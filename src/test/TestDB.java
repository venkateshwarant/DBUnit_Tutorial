
package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FullXmlDataFileLoader;
import org.junit.BeforeClass;
import org.junit.Test;




public class TestDB {

	public final static String LIVE_DB = "/DB.properties";

    @BeforeClass
    public static void setupTests() throws SQLException, IOException {
        DataSource dsrc = DataHelper.getDatasource(LIVE_DB);
        assertNotNull("No datasource", dsrc);
        Connection con = dsrc.getConnection();
        assertNotNull("No connection", con);
        Statement stmt = con.createStatement();
        assertNotNull("No statement", stmt);
        String sql = "select count(1) from INFORMATION_SCHEMA.SCHEMA_PRIVILEGES";
        ResultSet rs = stmt.executeQuery(sql);
        assertTrue("ResultSet is empty", rs.next());
        con.close();
    }
    /**
     * Lets load us up some authors!
     *
     * @throws SQLException
     */
    @Test
    public void loadData() throws SQLException {
        DataSource dsrc = DataHelper.getDatasource(LIVE_DB);

        IDatabaseConnection dbUnitCon = new DatabaseDataSourceConnection(dsrc);

        Map<String, String> substitutes = new HashMap<String, String>();
        substitutes.put("[null]", null);

        DataFileLoader loader = new FullXmlDataFileLoader(substitutes);

        IDataSet ds = loader.load("/testdata.xml");
        assertNotNull("No dataset found", ds);
        try {
            DatabaseOperation.REFRESH.execute(dbUnitCon, ds); // refresh of the DB.

        } catch (DatabaseUnitException e) {
            System.out.println("Failed to load the data: "+ e);
            System.out.println(e.getMessage());
        } finally {
            dbUnitCon.close();
        }

        Connection con = dsrc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TEST WHERE NAME='HELLO' ");
            assertTrue("ResultSet is empty", results.next());
            assertEquals("HELLO", results.getString("NAME"));
            assertEquals("WORLD", results.getString("VAL"));
        } finally {
            con.close();
        }
    }

    
    /**
     * Lets look for some data
     */
    @Test
    public void checkForData() throws SQLException, DataSetException {
        IDatabaseConnection dbunitConnection = new DatabaseDataSourceConnection(DataHelper.getDatasource(LIVE_DB));

        // Let's filter to keep only the tables we want
        String[] tables = {"TEST"};
        ITableFilter filter = new DatabaseSequenceFilter(dbunitConnection, tables);
        IDataSet dataset = new FilteredDataSet(filter, dbunitConnection.createDataSet());

        ITableIterator tabIT = dataset.iterator();
        ITable t;
        while (tabIT.next()) {
            t = tabIT.getTable();
            Column[] cols = tabIT.getTableMetaData().getColumns();
            System.out.println("Table name: "+ t.getTableMetaData().getTableName());
            for (int i = 0; i < t.getRowCount(); i++) {
            	System.out.println("Row "+ i);
                for (Column c : cols) {
                	System.out.println(c.getColumnName()+"\t"+ t.getValue(i, c.getColumnName()));
                }
            }
        }
    }


}
