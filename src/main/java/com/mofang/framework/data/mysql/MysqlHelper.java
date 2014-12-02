package com.mofang.framework.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mofang.framework.data.mysql.core.meta.ResultData;
import com.mofang.framework.data.mysql.core.meta.RowData;
import com.mofang.framework.data.mysql.core.meta.MysqlParameter;
import com.mysql.jdbc.Statement;

/**
 * Mysql帮助类
 * @author zhaodx111
 *
 */
public class MysqlHelper
{
	public static ResultData execute(Connection conn, MysqlParameter parameter) throws Exception
	{
		return execute(conn, parameter, false);
	}
	
	public static ResultData execute(Connection conn, MysqlParameter parameter, boolean needAutoIncrement) throws Exception
	{
		if(conn == null)
			throw new Exception("conn can not be null.");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Object autoIncrementResult = null;
		try 
		{
			pstmt = createPreparedStatement(conn, parameter, needAutoIncrement);
			int effectRows = pstmt.executeUpdate();
			boolean executeResult = effectRows > 0;
			
			if(needAutoIncrement)
			{
				rs = pstmt.getGeneratedKeys();
				if(rs.next())
					autoIncrementResult = rs.getObject(1);
			}
			
			ResultData result = new ResultData();
			result.setAutoIncrementResult(autoIncrementResult);
			result.setExecuteResult(executeResult);
			return result;
		} 
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			try 
			{
				if(null != rs)
					rs.close();
				if(null != pstmt)
					pstmt.close();
				if(null != conn)
					conn.close();
			} 
			catch (SQLException e) 
			{
				throw e;
			}
		}
	}

	public static ResultData executeTransaction(Connection conn, List<MysqlParameter> parameterList) throws Exception
	{
		if(conn == null)
			throw new Exception("conn can not be null.");
		
		ResultData result = new ResultData();
		PreparedStatement pstmt = null;
		try 
		{
			conn.setAutoCommit(false);
			for(MysqlParameter parameter : parameterList)
			{
				pstmt = createPreparedStatement(conn, parameter, false);
				pstmt.executeUpdate();
			}
			conn.commit();
			
			result.setExecuteResult(true);
			return result;
		} 
		catch(SQLException e)
		{
			conn.rollback();
			throw e;
		}
		finally
		{
			try 
			{
				if(null != pstmt)
					pstmt.close();
				if(null != conn)
				{
					conn.setAutoCommit(true);
					conn.close();
				}
			} 
			catch (SQLException e) 
			{
				throw e;
			}
		}
	}
	
	public static ResultData query(Connection conn, MysqlParameter parameter) throws Exception
	{
		if(null == conn)
			throw new Exception("conn can not be null.");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = createPreparedStatement(conn, parameter, false);
			rs = pstmt.executeQuery();
			if(null == rs)
				return null;
			
			ResultData result = new ResultData();
			while(rs.next())
			{
				int len = rs.getMetaData().getColumnCount();
				RowData rowData = new RowData(len);
				for(int i=1; i<=len; i++)
				{
					rowData.add(rs.getObject(i));
				}
				result.addRowData(rowData);
			}
			result.setExecuteResult(true);
			return result;
		} 
		catch (Exception e) 
		{
			throw e;
		}
		finally
		{
			try 
			{
				if(null != rs)
					rs.close();
				if(null != pstmt)
					pstmt.close();
				if(null != conn)
					conn.close();
			} 
			catch (SQLException e) 
			{ 
				throw e;
			}
		}
	}
	
	
	private static PreparedStatement createPreparedStatement(Connection conn, MysqlParameter parameter, boolean needAutoIncrement)throws Exception
	{
		try
		{
			PreparedStatement pstmt = null;
			if(needAutoIncrement)
				pstmt = conn.prepareStatement(parameter.getSql(), Statement.RETURN_GENERATED_KEYS);
			else
				pstmt = conn.prepareStatement(parameter.getSql());
			
			List<Object> params = parameter.getParams();
			if(null != params && params.size() > 0)
			{
				for(int i=0; i<params.size(); i++)
					pstmt.setObject(i + 1, params.get(i));
			}
			return pstmt;
		} 
		catch (SQLException e)
		{
			throw e;
		}
	}
}
