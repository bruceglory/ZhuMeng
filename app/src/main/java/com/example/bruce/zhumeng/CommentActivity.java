package com.example.bruce.zhumeng;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.bruce.zhumeng.adapter.CommentAdapter;
import com.example.bruce.zhumeng.model.entities.Comment;
import com.example.bruce.zhumeng.model.entities.CommentInfo;
import com.example.bruce.zhumeng.model.entities.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2016/4/22.
 */
public class CommentActivity extends AppCompatActivity {

    private static final int LOAD_DATA_SUCCESS = 0;

    private EditText          commentEt;
    private CommentAdapter    commentAdapter;
    private String            objectId;
    private String            databaseName;
    private List<CommentInfo> commentInfoList;
    private CommentHandler    commentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_mainview);
        objectId = getIntent().getStringExtra("objectId");
        databaseName = getIntent().getStringExtra("databaseName");
        Log.d("CommentActivity", "objcetId=" + objectId);
        init();
    }

    private void init() {
        ImageButton       commentSendIb;
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        commentHandler = new CommentHandler(this);
        commentEt = (EditText) findViewById(R.id.comment);
        commentSendIb = (ImageButton) findViewById(R.id.comment_send_button);
        commentSendIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CommentActivity", "imageButton click");
                String commentContent = commentEt.getText().toString();
                if (!TextUtils.isEmpty(commentContent)) {
                    createComment(commentContent);
                } else {
                    Toast.makeText(CommentActivity.this, R.string.send_comment_tip, Toast.LENGTH_SHORT).show();
                }

            }
        });
        initListView();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createComment(String commentContent) {
        final AVObject comment = new AVObject("Comment");
        comment.put("comment", commentContent);
        comment.put("target", AVObject.createWithoutData(databaseName, objectId));
        comment.put("targetUser", AVObject.createWithoutData("_User", AVUser.getCurrentUser().getObjectId()));
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.d("CommentActivity", "send comment success");
                    commentEt.setText(null);
                    loadCommentData(true);
                    hideSoftInput();
                } else {
                    Log.d("CommentActivity", "send comment failed=" + e.getMessage());
                }
            }
        });
    }

    private void initListView() {
        ListView          commentListView;
        commentInfoList = new ArrayList<>();
        commentListView = (ListView) findViewById(R.id.comment_list_view);
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
            }
        });

        commentListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInput();
                return false;
            }
        });
        loadCommentData(true);
        commentAdapter = new CommentAdapter(this, commentInfoList);
        commentListView.setAdapter(commentAdapter);
        commentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //not scroll
                    case SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            loadCommentData(false);
                        } else if (view.getFirstVisiblePosition() == 0) {
                            loadCommentData(true);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //noting
            }
        });
    }
    /*
     @para isLoadNewestData  is load the newest data
     */
    private void loadCommentData(final boolean isLoadNewestData) {
        int skip = commentInfoList.size();
        AVQuery<AVObject> query = new AVQuery<>("Comment");
        query.whereEqualTo("target", AVObject.createWithoutData(databaseName, objectId));
        query.orderByDescending("updatedAt");
        query.include("targetUser");
        query.setLimit(10);
        if (isLoadNewestData) {
            if (skip != 0) {
                CommentInfo first = commentInfoList.get(0);
                query.whereGreaterThan("updatedAt",first.getComment().getUpdateTime());
            }
        } else {
            if (skip != 0) {
                Log.d("CommentActivity","skip="+skip);
                CommentInfo last = commentInfoList.get(skip -1);
                query.whereLessThan("updatedAt",last.getComment().getUpdateTime());
            }
        }

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (isLoadNewestData) {
                        if (list.size() > 10) {
                            commentInfoList.clear();
                        } else {
                            for(int i = list.size()-1;i>=0;i--) {
                                AVObject comment = list.get(i);
                                CommentInfo commentInfo = parseCommentAVObject(comment);
                                commentInfoList.add(0,commentInfo);
                            }
                        }
                    } else {
                        for (AVObject comment : list) {
                            CommentInfo commentInfo = parseCommentAVObject(comment);
                            commentInfoList.add(commentInfo);
                        }
                        Log.d("CommentActivity", "commentInfo.size=" + commentInfoList.size());
                    }
                    if (list.size() > 0) {
                        commentHandler.sendEmptyMessage(LOAD_DATA_SUCCESS);
                    }
                } else {
                    Log.d("CommentActivity", "query comment fail,error = " + e.getMessage());
                }
            }
        });
    }

    private CommentInfo parseCommentAVObject(AVObject comment) {
        AVObject user = comment.getAVObject("targetUser");
        User targetUser = new User();
        Comment localComment = new Comment();
        targetUser.setUsername(user.getString("username"));
        Log.d("CommentActivity", "username=" + user.getString("username"));
        localComment.setComment(comment.getString("comment"));
        localComment.setUpdateTime(comment.getUpdatedAt());
        return new CommentInfo(localComment, targetUser);
    }
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            imm.hideSoftInputFromWindow(commentEt.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class CommentHandler extends Handler {
        WeakReference<CommentActivity> commentActivityWeakReference;

        public CommentHandler(CommentActivity commentActivity) {
            commentActivityWeakReference = new WeakReference<>(commentActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CommentActivity commentActivity = commentActivityWeakReference.get();
            if (commentActivity != null) {
                if (msg.what == LOAD_DATA_SUCCESS) {
                    commentActivity.commentAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
