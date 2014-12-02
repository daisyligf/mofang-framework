package com.mofang.framework.data.mysql.core.criterion.operand;

import com.mofang.framework.data.mysql.core.criterion.type.SqlClauseType;

/**
 * 
 * @author zhaodx
 *
 */
public class ClauseOperand extends Operand 
{
	private SqlClauseType sqlClauseType = SqlClauseType.None;

    public ClauseOperand(SqlClauseType sqlClauseType)
    {
        this.sqlClauseType = sqlClauseType;
    }

    public String toString()
    {
        if (null == operands)
        	sqlClauseType = SqlClauseType.None;

        return super.toString();
    }

	@Override
	protected String toExpression()
	{
		String strSqlClause = sqlClauseType == SqlClauseType.None ? "" : sqlClauseType.toString();
        return String.format("%s ", strSqlClause);
	}
}