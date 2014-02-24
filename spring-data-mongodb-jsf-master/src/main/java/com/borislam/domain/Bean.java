package com.borislam.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean
@ViewScoped
public class Bean implements Serializable
{
    private List<Field> m_lFields;

    public Bean()
    {
        m_lFields = new ArrayList();

        m_lFields.add(new Field());
    }

    public void setFields(List<Field> p_lFields)
    {
        m_lFields = p_lFields;
    }

    public List<Field> getFields()
    {
        return m_lFields;
    }

    public void onButtonRemoveFieldClick(final Field p_oField)
    {
        m_lFields.remove(p_oField);
    }

    public void onButtonAddFieldClick(AjaxBehaviorEvent p_oEvent)
    {
        m_lFields.add(new Field());
    }
}