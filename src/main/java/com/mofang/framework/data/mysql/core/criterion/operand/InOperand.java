package com.mofang.framework.data.mysql.core.criterion.operand;

import java.util.Collection;

import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class InOperand extends Operand
{
    private String columnName;
    private Collection<?> columnValue;

    /**
     * 
     * @param columnName 
     * @param columnValue  example: 1,2,3,4,5,6
     */
    public InOperand(String columnName, Collection<?> columnValue)
    {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

	@Override
	protected String toExpression()
	{
		String list = StringUtil.join(columnValue);
		return String.format("%s IN (%s) ",columnName, list);
	}
}