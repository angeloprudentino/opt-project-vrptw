package com.softtechdesign.ga;

/**
 * Custom GA exception class
 * @author Jeff Smith jeff@SoftTechDesign.com
 */
@SuppressWarnings("serial")

public class GAException extends Exception
{
    /**
     * GAException constructor
     * @param msg
     */
    GAException(String msg)
    {
        super(msg);
    }
}
