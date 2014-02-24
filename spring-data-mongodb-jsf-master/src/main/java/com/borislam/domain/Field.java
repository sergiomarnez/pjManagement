package com.borislam.domain;

import java.io.Serializable;

public class Field implements Serializable
{
    private String m_sName;

    public void setName(String p_sName)
    {
        m_sName = p_sName;
    }

    public String getName()
    {
        return m_sName;
    }
}