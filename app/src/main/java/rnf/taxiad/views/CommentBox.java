package rnf.taxiad.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import rnf.taxiad.R;

/**
 * Created by Rahil on 13/1/16.
 */
public class CommentBox extends RelativeLayout implements TextWatcher {

    private EditText edtComments;
    private TextView tvComments;

    public CommentBox(Context context) {
        super(context);
    }

    public CommentBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.view_comment_box, this, false);
        edtComments = (EditText) view.findViewById(R.id.edtComments);
        tvComments = (TextView) view.findViewById(R.id.tvComments);
        edtComments.addTextChangedListener(this);
        this.addView(view);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        final String text = String.format(getContext().getString(R.string.comment_box_length), edtComments.getText().length());
        tvComments.setText(text);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText() {
        return edtComments.getText().toString();
    }

    public EditText getEditText() {
        return edtComments;
    }
}
