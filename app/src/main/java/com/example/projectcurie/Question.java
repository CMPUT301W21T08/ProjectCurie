package com.example.projectcurie;
/**
 * Questions are represented by this class and inherits attributes from Comment.
 * @author Bo Cen
 */
public class Question extends Comment{
    public Question(String body, String poster) {
        super(body, poster);
    }

    public void Reply(String body, String poster){
        //reply method goes here
    }
}
