package com.Utilities.General;

import java.util.Stack;

/**
 * Created by Wei.Lu on 9/2/2016.
 */
public class DeleteData <E>{
    private Stack<E> deleteDataStack = new Stack<>();

    public void setData(E e){
        deleteDataStack.push(e);
    }

    public E getData(){
        return deleteDataStack.pop();
    }

    public boolean isEmpty(){return deleteDataStack.isEmpty();}
}
