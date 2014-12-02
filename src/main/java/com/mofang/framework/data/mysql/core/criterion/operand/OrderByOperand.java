package com.mofang.framework.data.mysql.core.criterion.operand;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class OrderByOperand extends Operand
{
    private List<OrderByEntry> entries;

    public OrderByOperand(OrderByEntry entry)
    {
    	List<OrderByEntry> entryList = new ArrayList<OrderByEntry>();
    	entryList.add(entry);
        this.entries = entryList;
    }
    
    public OrderByOperand(List<OrderByEntry> entries)
    {
        this.entries = entries;
    }

	@Override
	protected String toExpression()
	{
		if (null == entries || entries.size() == 0)
	        return "";

		StringBuilder strOrderBy = new StringBuilder();
		for(OrderByEntry entry : entries)
			strOrderBy.append(String.format("%s %s,", entry.getColumnName(), entry.getSortType().toString()));
	    
		return String.format("Order By %s ", strOrderBy.substring(0, strOrderBy.length() - 1));
	}
}