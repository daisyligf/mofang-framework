package com.mofang.framework.data.mysql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.mofang.framework.data.mysql.core.meta.RowData;
import com.mofang.framework.data.mysql.MysqlExecutor;
import com.mofang.framework.data.mysql.MysqlHelper;
import com.mofang.framework.data.mysql.core.meta.MysqlParameter;
import com.mofang.framework.data.mysql.core.meta.ResultData;
import com.mofang.framework.data.mysql.core.meta.TableColumnMetaData;
import com.mofang.framework.data.mysql.core.meta.TableMetaData;
import com.mofang.framework.data.mysql.core.annotation.AutoIncrement;
import com.mofang.framework.data.mysql.core.annotation.ColumnName;
import com.mofang.framework.data.mysql.core.annotation.PrimaryKey;
import com.mofang.framework.data.mysql.core.annotation.TableName;
import com.mofang.framework.data.mysql.core.criterion.operand.Operand;
import com.mofang.framework.data.mysql.pool.MysqlPool;

/**
 * 
 * @author zhaodx
 *
 */
public abstract class AbstractMysqlSupport<P>
{
	private Class<P> modelClass;
	private Connection conn;
	private MysqlPool pool;
	
	public void setConn(Connection conn) throws Exception 
	{
		if(null == conn)
			throw new Exception("mysql connection can not be null.");
		
		this.conn = conn;
	}
	
	public void setMysqlPool(MysqlPool pool) throws Exception 
	{
		if(null == pool)
			throw new Exception("mysql pool can not be null.");
		
		this.pool = pool;
	}

	@SuppressWarnings("unchecked")
	public AbstractMysqlSupport()
	{
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		modelClass = (Class<P>) type.getActualTypeArguments()[0];
	}
	
	public boolean insert(P model) throws Exception
	{
		return insert(model, null);
	}
	
	public boolean insert(P model, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		StringBuilder strValues = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		strSql.append("insert into " + metaData.getTableName() + "(");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			if(!entry.isAutoIncrementKey())
			{
				strFields.append(columnName + ",");
				strValues.append("?,");
				objValues.add(entry.getFieldValue());
			}
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(") values (");
		strSql.append(strValues.substring(0, strValues.length() - 1));
		strSql.append(")");
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public boolean insert(P model, boolean needAutoIncrement) throws Exception
	{
		return insert(model, null, needAutoIncrement);
	}
	
	public boolean insert(P model, Set<String> ignoreFields, boolean needAutoIncrement) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		StringBuilder strValues = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		strSql.append("insert into " + metaData.getTableName() + "(");
		String columnName;
		String autoIncrementName = null;
		Class<?> autoIncrementType = null;
		
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			if(!entry.isAutoIncrementKey())
			{
				strFields.append(columnName + ",");
				strValues.append("?,");
				objValues.add(entry.getFieldValue());
			}
			else
			{
				autoIncrementName = entry.getFieldName();
				autoIncrementType = entry.getFieldType();
			}
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(") values (");
		strSql.append(strValues.substring(0, strValues.length() - 1));
		strSql.append(")");
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter, needAutoIncrement);
		if(null == result)
			return false;
		
		if(needAutoIncrement)
		{
			Field field = model.getClass().getDeclaredField(autoIncrementName);
			if(null != field)
			{
				Object value = result.getAutoIncrementResult();
				setModelValue(field, model, value, autoIncrementType);
			}
		}
		return result.getExecuteResult();
	}
	
	public Future<ResultData> invokeInsert(P model) throws Exception
	{
		return invokeInsert(model, null);
	}
	
	public Future<ResultData> invokeInsert(P model, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		StringBuilder strValues = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		strSql.append("insert into " + metaData.getTableName() + "(");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			if(!entry.isAutoIncrementKey())
			{
				strFields.append(columnName + ",");
				strValues.append("?,");
				objValues.add(entry.getFieldValue());
			}
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(") values (");
		strSql.append(strValues.substring(0, strValues.length() - 1));
		strSql.append(")");
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public boolean updateByPrimaryKey(P model) throws Exception
	{
		return updateByPrimaryKey(model, null);
	}
	
	public boolean updateByPrimaryKey(P model, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		StringBuilder strWheres = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		List<Object> objWheres = new ArrayList<Object>();
		strSql.append("update " + metaData.getTableName() + " set ");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			strFields.append(columnName + " = ?,");
			objValues.add(entry.getFieldValue());
			if(entry.isPrimaryKey())
			{
				strWheres.append("and " + columnName + " = ? ");
				objWheres.add(entry.getFieldValue());
			}
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" where " + strWheres.substring(4));
		objValues.addAll(objWheres);
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public boolean updateByWhere(P model, Operand operand) throws Exception
	{
		return updateByWhere(model, operand);
	}
	
	public boolean updateByWhere(P model, Operand operand, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		strSql.append("update " + metaData.getTableName() + " set ");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			strFields.append(columnName + " = ?,");
			objValues.add(entry.getFieldValue());
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" " + operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public Future<ResultData> invokeUpdateByPrimaryKey(P model) throws Exception
	{
		return invokeUpdateByPrimaryKey(model, null);
	}
	
	public Future<ResultData> invokeUpdateByPrimaryKey(P model, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		StringBuilder strWheres = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		List<Object> objWheres = new ArrayList<Object>();
		strSql.append("update " + metaData.getTableName() + " set ");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			strFields.append(columnName + " = ?,");
			objValues.add(entry.getFieldValue());
			if(entry.isPrimaryKey())
			{
				strWheres.append("and " + columnName + " = ? ");
				objWheres.add(entry.getFieldValue());
			}
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" where " + strWheres.substring(4));
		objValues.addAll(objWheres);
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public Future<ResultData> invokeUpdateByWhere(P model, Operand operand) throws Exception
	{
		return invokeUpdateByWhere(model, operand, null);
	}
	
	public Future<ResultData> invokeUpdateByWhere(P model, Operand operand, Set<String> ignoreFields) throws Exception
	{
		TableMetaData metaData = getMetaData(model);
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		List<Object> objValues = new ArrayList<Object>();
		strSql.append("update " + metaData.getTableName() + " set ");
		String columnName;
		for(TableColumnMetaData entry : columns)
		{
			columnName = entry.getColumnName();
			if(null == columnName || "".equals(columnName))
				continue;
			
			if(null != ignoreFields && ignoreFields.contains(columnName))
				continue;
			
			strFields.append(columnName + " = ?,");
			objValues.add(entry.getFieldValue());
		}
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" " + operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public boolean deleteByWhere(Operand operand) throws Exception
	{
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + metaData.getTableName() + " ");
		if(null != operand)
			strSql.append(operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), null);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public boolean deleteByPrimaryKey(Object pkValue) throws Exception
	{
		if(null == pkValue)
			throw new Exception("primarykey value can not be null");
		
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		String pkName = metaData.getPrimaryKey();
		if(null == pkName || "".equals(pkName))
			throw new Exception("table primary key can not be null.");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + metaData.getTableName() + " ");
		strSql.append("where " + pkName + " = ?");
		List<Object> objValues = new ArrayList<Object>();
		objValues.add(pkValue);
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public Future<ResultData> invokeDeleteByWhere(Operand operand) throws Exception
	{
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + metaData.getTableName() + " ");
		if(null != operand)
			strSql.append(operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), null);
		if(null != pool)
			conn = pool.getConnection();
		
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public Future<ResultData> invokeDeleteByPrimaryKey(Object pkValue) throws Exception
	{
		if(null == pkValue)
			throw new Exception("primarykey value can not be null");
		
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		String pkName = metaData.getPrimaryKey();
		if(null == pkName || "".equals(pkName))
			throw new Exception("table primary key can not be null.");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("delete from " + metaData.getTableName() + " ");
		strSql.append("where " + pkName + " = ?");
		List<Object> objValues = new ArrayList<Object>();
		objValues.add(pkValue);
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public P getByPrimaryKey(Object pkValue) throws Exception
	{
		if(null == pkValue)
			throw new Exception("primarykey value can not be null");
		
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		String pkName = metaData.getPrimaryKey();
		if(null == pkName || "".equals(pkName))
			throw new Exception("table primary key can not be null.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		strSql.append("select ");
		for(TableColumnMetaData entry : columns)
			strFields.append(entry.getColumnName() + ",");
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" from " + metaData.getTableName() + " ");
		strSql.append("where " + pkName + " = ? ");
		strSql.append("limit 1");
		List<Object> objValues = new ArrayList<Object>();
		objValues.add(pkValue);
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), objValues);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlHelper.query(conn, parameter);
		if(null == result || null == result.getQueryResult() || result.getQueryResult().size() == 0)
			return null;
		return convertDataEntryToModel(metaData, result.getQueryResult().get(0));
	}
	
	public List<P> getList(Operand operand) throws Exception
	{
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		StringBuilder strSql = new StringBuilder();
		StringBuilder strFields = new StringBuilder();
		strSql.append("select ");
		for(TableColumnMetaData entry : columns)
			strFields.append(entry.getColumnName() + ",");
		
		strSql.append(strFields.substring(0, strFields.length() - 1));
		strSql.append(" from " + metaData.getTableName() + " ");
		if(null != operand)
			strSql.append(operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), null);
		if(null != pool)
			conn = pool.getConnection();
		
		ResultData result = MysqlHelper.query(conn, parameter);
		if(null == result)
			return null;
		
		List<RowData> rows = result.getQueryResult();
		if(null == rows || rows.size() == 0)
			return null;
		
		List<P> modelList = new ArrayList<P>();
		P model = null;
		for(RowData entry : rows)
		{
			model = convertDataEntryToModel(metaData, entry);
			if(null == model)
				continue;
			
			modelList.add(model);
		}
		return modelList;
	}
	
	public Long getCount(Operand operand) throws Exception
	{
		TableMetaData metaData = getMetaData();
		if(null == metaData)
			throw new Exception("table meta data get fail.");
		
		StringBuilder strSql = new StringBuilder();
		strSql.append("select count(1) ");
		strSql.append("from " + metaData.getTableName() + " ");
		if(null != operand)
			strSql.append(operand.toString());
		
		MysqlParameter parameter = new MysqlParameter(strSql.toString(), null);
		if(null != pool)
			conn = pool.getConnection();
		ResultData result = MysqlHelper.query(conn, parameter);
		if(null == result)
			return null;
		
		List<RowData> rows = result.getQueryResult();
		if(null == rows || rows.size() == 0)
			return null;
		
		RowData entry = rows.get(0);
		return entry.getLong(0);
	}
	
	public boolean execute(String strSql) throws Exception
	{
		MysqlParameter parameter = new MysqlParameter(strSql, null);
		if(null != pool)
			conn = pool.getConnection();
		ResultData result = MysqlExecutor.getInstance().execute(conn, parameter);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public Future<ResultData> invokeExecute(String strSql) throws Exception
	{
		MysqlParameter parameter = new MysqlParameter(strSql, null);
		if(null != pool)
			conn = pool.getConnection();
		return MysqlExecutor.getInstance().invokeExecute(conn, parameter);
	}
	
	public ResultData executeQuery(String strSql) throws Exception
	{
		MysqlParameter parameter = new MysqlParameter(strSql, null);
		if(null != pool)
			conn = pool.getConnection();
		return MysqlHelper.query(conn, parameter);
	}
	
	public boolean executeTransaction(List<String> sqlList) throws Exception
	{
		List<MysqlParameter> parameterList = new ArrayList<MysqlParameter>();
		for(String strSql : sqlList)
			parameterList.add(new MysqlParameter(strSql, null));
		
		if(null != pool)
			conn = pool.getConnection();
		ResultData result = MysqlHelper.executeTransaction(conn, parameterList);
		if(null == result)
			return false;
		return result.getExecuteResult();
	}
	
	public Future<ResultData> invokeExecuteTransaction(List<String> sqlList) throws Exception
	{
		List<MysqlParameter> parameterList = new ArrayList<MysqlParameter>();
		for(String strSql : sqlList)
			parameterList.add(new MysqlParameter(strSql, null));
		
		if(null != pool)
			conn = pool.getConnection();
		return MysqlExecutor.getInstance().invokeExecuteTransaction(conn, parameterList);
	}
	
 	private TableMetaData getMetaData() throws Exception
	{
		Field[] fields = modelClass.getDeclaredFields();
		if(null == fields || fields.length == 0)
			return null;
		
		TableName tableName = modelClass.getAnnotation(TableName.class);
		if(null == tableName)
			throw new Exception("table name can not be null.");
		
		TableMetaData metaData = new TableMetaData();
		List<TableColumnMetaData> columns = new ArrayList<TableColumnMetaData>();
		TableColumnMetaData column = null;
		String fieldName = null;
		Object fieldValue = null;
		Class<?> fieldType = null;
		for(Field field : fields)
		{
			fieldName = field.getName();
			field.setAccessible(true);
			fieldType = field.getType();
			
			String columnName = "";
			ColumnName dataField = field.getAnnotation(ColumnName.class);
			if(null != dataField)
				columnName = dataField.name();
			
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			Boolean isPrimaryKey = false;
			if(null != primaryKey)
			{
				isPrimaryKey = true;
				metaData.setPrimaryKey(columnName);
			}
			
			column = new TableColumnMetaData();
			column.setFieldName(fieldName);
			column.setFieldType(fieldType);
			column.setFieldValue(fieldValue);
			column.setColumnName(columnName);
			column.setColumnType(null);
			column.setPrimaryKey(isPrimaryKey);
			columns.add(column);
		}
		metaData.setTableName(tableName.name());
		metaData.setColumns(columns);
		return metaData;
	}
	
	private TableMetaData getMetaData(P model) throws Exception
	{
		Field[] fields = modelClass.getDeclaredFields();
		if(null == fields || fields.length == 0)
			return null;
		
		TableName tableName = modelClass.getAnnotation(TableName.class);
		if(null == tableName)
			throw new Exception("table name can not be null.");
		
		TableMetaData metaData = new TableMetaData();
		List<TableColumnMetaData> columns = new ArrayList<TableColumnMetaData>();
		TableColumnMetaData column = null;
		String fieldName = null;
		Object fieldValue = null;
		Class<?> fieldType = null;
		for(Field field : fields)
		{
			fieldName = field.getName();
			field.setAccessible(true);
			fieldValue = field.get(model);
			fieldType = field.getType();
			
			String columnName = "";
			ColumnName dataField = field.getAnnotation(ColumnName.class);
			if(null != dataField)
				columnName = dataField.name();
			
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			Boolean isPrimaryKey = false;
			if(null != primaryKey)
			{
				isPrimaryKey = true;
				metaData.setPrimaryKey(columnName);
			}
			
			AutoIncrement autoIncrementKey = field.getAnnotation(AutoIncrement.class);
			Boolean isAutoIncrementKey = false;
			if(null != autoIncrementKey)
			{
				isAutoIncrementKey = true;
				metaData.setAutoIncrementKey(columnName);
			}
			
			column = new TableColumnMetaData();
			column.setFieldName(fieldName);
			column.setFieldType(fieldType);
			column.setFieldValue(fieldValue);
			column.setColumnName(columnName);
			column.setColumnType(null);
			column.setPrimaryKey(isPrimaryKey);
			column.setAutoIncrementKey(isAutoIncrementKey);
			columns.add(column);
		}
		metaData.setTableName(tableName.name());
		metaData.setColumns(columns);
		return metaData;
	}

	private P convertDataEntryToModel(TableMetaData metaData, RowData entry) throws Exception
	{
		P model = modelClass.newInstance();
		List<TableColumnMetaData> columns = metaData.getColumns();
		if(null == columns || columns.size() == 0)
			throw new Exception("table columns can not be null or empty.");
		
		int len = columns.size();
		TableColumnMetaData columnData = null;
		Class<?> fieldType = null;
		String fieldName = null;
		for(int i=0; i<len; i++)
		{
			columnData = columns.get(i);
			fieldType = columnData.getFieldType();
			fieldName = columnData.getFieldName();
			Field field = modelClass.getDeclaredField(fieldName);
			Object fieldValue = entry.get(i);
			setModelValue(field, model, fieldValue, fieldType);
		}
		return model;
	}
	
	private void setModelValue(Field field, P model, Object value, Class<?> fieldType) throws Exception
	{
		if(null == value)
			return;
		
		field.setAccessible(true);
		try
		{	
			if(fieldType.equals(String.class))
				field.set(model, value.toString());
			else if(fieldType.equals(Integer.class) || fieldType.equals(int.class))
				field.set(model, Integer.valueOf(value.toString()));
			else if(fieldType.equals(Double.class) || fieldType.equals(double.class))
				field.set(model, Double.valueOf(value.toString()));
			else if(fieldType.equals(Float.class) || fieldType.equals(float.class))
				field.set(model, Float.valueOf(value.toString()));
			else if(fieldType.equals(Long.class) || fieldType.equals(long.class))
				field.set(model, Long.valueOf(value.toString()));
			else if(fieldType.equals(Short.class) || fieldType.equals(short.class))
				field.set(model, Short.valueOf(value.toString()));
			else if(fieldType.equals(Boolean.class) || fieldType.equals(boolean.class))
				field.set(model, Boolean.valueOf(value.toString()));
			else if(fieldType.equals(Character.class) || fieldType.equals(char.class))
				field.set(model, (Character)value);
			else if(fieldType.equals(Byte.class) || fieldType.equals(byte.class))
				field.set(model, Byte.valueOf(value.toString()));
			else if(fieldType.equals(Byte[].class) || fieldType.equals(byte[].class))
				field.set(model, (Byte[])value);
			else if(fieldType.equals(java.util.Date.class))
				field.set(model, (java.util.Date)value);
			else if(fieldType.equals(java.sql.Timestamp.class))
				field.set(model, (java.sql.Timestamp)value);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}