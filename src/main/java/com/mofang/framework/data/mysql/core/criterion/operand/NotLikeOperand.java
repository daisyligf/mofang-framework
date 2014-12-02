package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class NotLikeOperand extends Operand
{
    private String columnName;
    private Object columnValue;

    public NotLikeOperand(String columnName, Object columnValue)
    {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

	@Override
	protected String toExpression() 
	{
		return String.format("%s NOT LIKE '%s' ", columnName, columnValue);
	}
}