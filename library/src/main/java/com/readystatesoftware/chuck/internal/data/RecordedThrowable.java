package com.readystatesoftware.chuck.internal.data;

import com.readystatesoftware.chuck.internal.support.FormatUtils;

import java.util.Date;

import nl.qbusict.cupboard.annotation.Index;

/**
 * @author Olivier Perez
 */
public class RecordedThrowable {


    public static final String[] PARTIAL_PROJECTION = new String[]{
            "_id",
            "tag",
            "clazz",
            "message",
            "date"
    };

    private Long _id;

    private String tag;

    @Index
    private Date date;

    private String clazz;

    private String message;

    private String content;

    @SuppressWarnings("unused")
    public RecordedThrowable() {
    }

    public RecordedThrowable(String tag, Throwable throwable) {
        this.tag = tag;
        date = new Date();
        clazz = throwable.getClass().getName();
        message = throwable.getMessage();
        content = FormatUtils.formatThrowable(throwable);
    }

    public Long getId() {
        return _id;
    }

    public RecordedThrowable setId(Long _id) {
        this._id = _id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public RecordedThrowable setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMessage() {
        return message;
    }

    public RecordedThrowable setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordedThrowable that = (RecordedThrowable) o;

        if (_id != null ? !_id.equals(that._id) : that._id != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

}
