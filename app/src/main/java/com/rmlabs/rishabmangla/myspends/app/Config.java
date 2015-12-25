package com.rmlabs.rishabmangla.myspends.app;

/**
 * Created by rishabmangla on 25/12/15.
 */
public class Config {

    //add the bank names here
    public static final String[] BANK_NAMES = {"hdfc","axis","icici","hsbc","yes","pnb","sbi","way"};
    //keywords used to determine the amount withdrawn messages
    public static final String[] MSG_KEYWORDS = {"withdrawn","debited"};

    // special character to prefix amount debited. assumption: this is the amount spend
    public static final String RS_DELIMITER = "rs";
    public static final String INR_DELIMITER = "inr";

}
