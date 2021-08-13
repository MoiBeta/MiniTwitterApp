
package com.example.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUploadPhoto {

    @SerializedName("fieldname")
    @Expose
    private String fieldname;
    @SerializedName("originalname")
    @Expose
    private String originanname;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private Integer size;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ResponseUploadPhoto() {
    }

    /**
     * 
     * @param fieldname
     * @param path
     * @param filename
     * @param size
     * @param originanname
     * @param destination
     * @param mimetype
     * @param encoding
     */
    public ResponseUploadPhoto(String fieldname, String originanname, String encoding, String mimetype, String destination, String filename, String path, Integer size) {
        super();
        this.fieldname = fieldname;
        this.originanname = originanname;
        this.encoding = encoding;
        this.mimetype = mimetype;
        this.destination = destination;
        this.filename = filename;
        this.path = path;
        this.size = size;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getOriginanname() {
        return originanname;
    }

    public void setOriginanname(String originanname) {
        this.originanname = originanname;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
