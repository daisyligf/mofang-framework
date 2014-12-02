package com.mofang.framework.data.mysql.core.criterion.operand;

import com.mofang.framework.util.StringUtil;

/**
 * 
 * @author zhaodx
 *
 */
public class GroupByOperand extends Operand
{
    private String[] columnNames;

    public GroupByOperand(String[] columnNames)
    {
        this.columnNames = columnNames;
    }

	@Override
	protected String toExpression()
	{
		if (null == columnNames || columnNames.length == 0)
            return "";

		
	    return String.format("GROUP BY %s ", StringUtil.join(columnNames));
	}
}