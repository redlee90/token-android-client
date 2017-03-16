package com.tokenbrowser.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tokenbrowser.model.local.SendState;
import com.tokenbrowser.token.R;
import com.tokenbrowser.view.adapter.listeners.OnItemClickListener;
import com.tokenbrowser.view.custom.RoundCornersImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextViewHolder extends RecyclerView.ViewHolder {

    private View localContainer;
    private TextView localText;
    private TextView remoteText;
    private TextView sentStatusMessage;
    private RoundCornersImageView localImage;
    private RoundCornersImageView remoteImage;

    private String text;
    private boolean sentByLocal;
    private @SendState.State int sendState;
    private String attachmentFilePath;

    public TextViewHolder(final View v) {
        super(v);
        this.localContainer = v.findViewById(R.id.local_container);
        this.localText = (TextView) v.findViewById(R.id.local_message);
        this.remoteText = (TextView) v.findViewById(R.id.remote_message);
        this.sentStatusMessage = (TextView) v.findViewById(R.id.sent_status_message);
        this.localImage = (RoundCornersImageView) v.findViewById(R.id.local_image);
        this.remoteImage = (RoundCornersImageView) v.findViewById(R.id.remote_image);
    }

    public TextViewHolder setText(final String text) {
        this.text = text;
        return this;
    }

    public TextViewHolder setSentByLocal(final boolean sentByLocal) {
        this.sentByLocal = sentByLocal;
        return this;
    }

    public TextViewHolder setSendState(final @SendState.State int sendState) {
        this.sendState = sendState;
        return this;
    }

    public TextViewHolder setAttachmentFilePath(final String filePath) {
        this.attachmentFilePath = filePath;
        return this;
    }

    public TextViewHolder draw() {
        if (this.sentByLocal) {
            hideRemoteView();

            if (this.attachmentFilePath == null) {
                showText(this.localText);
                hideImageViews();
            } else {
                showImage(this.localImage);
                hideTextViews();
            }

            setSendState();

        } else {
            hideLocalView();
            if (this.attachmentFilePath == null) {
                showText(this.remoteText);
                hideImageViews();
            } else {
                showImage(this.remoteImage);
                hideTextViews();
            }
        }

        return this;
    }

    private void hideRemoteView() {
        this.remoteText.setVisibility(View.GONE);
    }

    private void hideLocalView() {
        this.localContainer.setVisibility(View.GONE);
    }

    private void showText(final TextView tv) {
        if (this.sentByLocal) {
            this.localContainer.setVisibility(View.VISIBLE);
            this.sentStatusMessage.setVisibility(View.GONE);
        }

        tv.setVisibility(View.VISIBLE);
        tv.setText(this.text);
    }

    private void hideImageViews() {
        this.remoteImage.setVisibility(View.GONE);
        this.localImage.setVisibility(View.GONE);
    }

    private void hideTextViews() {
        this.localText.setVisibility(View.GONE);
        this.remoteText.setVisibility(View.GONE);
    }

    private void showImage(final RoundCornersImageView imageView) {
        if (this.sentByLocal) {
            this.remoteImage.setVisibility(View.GONE);
            this.localImage.setVisibility(View.VISIBLE);
        } else {
            this.localImage.setVisibility(View.GONE);
            this.remoteImage.setVisibility(View.VISIBLE);
        }

        final File imageFile = new File(this.attachmentFilePath);
        imageView.setImage(imageFile);

        this.attachmentFilePath = null;
    }

    private void setSendState() {
        if (this.sendState == SendState.STATE_FAILED || this.sendState == SendState.STATE_PENDING) {
            this.sentStatusMessage.setVisibility(View.VISIBLE);
            this.sentStatusMessage.setText(this.sendState == SendState.STATE_FAILED
                    ? R.string.error__message_failed
                    : R.string.error__message_pending);
        }
    }

    public TextViewHolder setClickableImage(final OnItemClickListener<String> listener, final String filePath) {
        this.localImage.setOnClickListener(v -> listener.onItemClick(filePath));
        this.remoteImage.setOnClickListener(v -> listener.onItemClick(filePath));
        return this;
    }

    public void setClickableUsernames(final OnItemClickListener<String> listener) {
        if (this.text == null) {
            return;
        }

        final SpannableString spannableString = new SpannableString(this.text);
        int lastEndPos = 0;

        for (final String word : getUsernames()) {
            final int currentStartPos = this.text.indexOf(word, lastEndPos);
            final int currentEndPos = this.text.indexOf(word, lastEndPos) + word.length();

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(final View view) {
                   handleSpannedClicked(view, listener, this);
                }
            }, currentStartPos,
               currentEndPos,
               Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            lastEndPos = currentEndPos;
        }

        this.localText.setText(spannableString);
        this.localText.setMovementMethod(LinkMovementMethod.getInstance());
        this.remoteText.setText(spannableString);
        this.remoteText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private List<String> getUsernames() {
        final Pattern pattern = Pattern.compile("(?:^|\\s)@(\\w+)");
        final Matcher matcher = pattern.matcher(this.text);
        final List<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        return matches;
    }

    private void handleSpannedClicked(final View view,
                                      final OnItemClickListener<String> listener,
                                      final ClickableSpan clickableSpan) {
        final TextView tv = (TextView) view;
        final Spanned spannedString = (Spanned) tv.getText();
        final String username =
                spannedString
                        .subSequence(
                                spannedString.getSpanStart(clickableSpan),
                                spannedString.getSpanEnd(clickableSpan))
                        .toString();

        listener.onItemClick(username);
    }
}
