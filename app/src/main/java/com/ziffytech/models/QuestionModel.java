package com.ziffytech.models;

/**
 * Created by Mahesh on 09/01/18.
 */

public class QuestionModel {

    public QuestionModel() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String subject;
    private String description;
    private String id;

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    private String replyBy;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    private String doctorName;

    public String getQ_reply_id() {
        return q_reply_id;
    }

    public void setQ_reply_id(String q_reply_id) {
        this.q_reply_id = q_reply_id;
    }

    public String getQ_read() {
        return q_read;
    }

    public void setQ_read(String q_read) {
        this.q_read = q_read;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    private String q_reply_id;
    private String q_read; // 0 means show gray else white
    private String created;

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    private String doctor_id;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag; //flag 0 means hide reply text else show



}
