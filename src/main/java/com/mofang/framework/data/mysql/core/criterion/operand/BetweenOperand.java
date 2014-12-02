package com.mofang.framework.data.mysql.core.criterion.operand;

/**
 * 
 * @author zhaodx
 *
 */
public class BetweenOperand extends Operand
{
	private String columnName;
	private Object lowerValue;
	private Object higherValue;
	
	public BetweenOperand(String columnName, Object lowerValue, Object higherValue)
	{
		this.columnName = columnName;
		this.lowerValue = lowerValue;
		this.higherValue = higherValue;
	}

	@Override
	protected String toExpression()
	{
		return String.format("{0} BETWEEN {1} AND {2} ", columnName, lowerValue, higherValue);
	}
}