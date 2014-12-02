package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class EqualOperand extends Operand
{
    private String columnName;
    private Object columnValue;

    public EqualOperand(String columnName, Object columnValue)
    {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

	@Override
	protected String toExpression()
	{
		return String.format("%s = %s ", columnName, columnValue);
	}
}