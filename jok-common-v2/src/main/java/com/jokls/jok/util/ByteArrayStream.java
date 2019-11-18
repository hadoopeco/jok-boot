package com.jokls.jok.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/19 16:41
 */
public class ByteArrayStream {
    protected byte[] buf;
    protected int count;

    public ByteArrayStream() {
        this(32);
    }

    public ByteArrayStream(int size){
        if(size < 0){
            throw new IllegalArgumentException("Negative initial size : "+size );
        } else {
            this.buf = new byte[size];
        }
    }

    public void write(int b){
        int tmpcount = this.count + 1;
        if(tmpcount > this.buf.length){
            byte[] tmpbuf = new byte[Math.max(this.buf.length << 1, tmpcount)];
            System.arraycopy(this.buf, 0, tmpbuf, 0 , this.count);
            this.buf = tmpbuf;
        }

        this.buf[this.count] = (byte)b;
        this.count = tmpcount;
    }

    public void write(byte[] b) {this.write(b, 0, b.length);}

    public void write(byte[] b, int off, int len){
        if(off >=0 && off <= b.length && len >=0 && off + len <= b.length && off + len >=0 ){
            if(len != 0){
                int tmpcount  = this.count + len;
                if( tmpcount > this.buf.length){
                    byte[] tmpbuf = new byte[Math.max(this.buf.length << 1 , tmpcount)];
                    System.arraycopy(this.buf, 0 , tmpbuf, 0, this.count);
                    this.buf = tmpbuf;
                }

                System.arraycopy(b, off, this.buf, this.count, len);
                this.count = tmpcount;
            }
        }else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    public void reset(){
        this.count = 0;
    }

    public byte[] toByteArray(){
        byte[] bytes = new byte[this.count];
        System.arraycopy(this.buf, 0, bytes, 0, this.count);
        return bytes;
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }


    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(this.buf, 0, this.count, enc);
    }

    public void close() throws IOException {
    }

 }
