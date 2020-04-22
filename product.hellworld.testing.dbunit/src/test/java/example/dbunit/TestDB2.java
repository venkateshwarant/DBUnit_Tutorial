package test.java.example.dbunit;


import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;



public class TestDB2 extends DBTestCase {

	
	public final static String LIVE_DB = "/DB.properties";

   
	
	public TestDB2(String name) {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://192.168.33.14:3306/hellodb");
        //System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "hellodb");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "ac");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "ac");
   
	}
	
	
	 /**
     * Other way to load data into the DB
     *
     */
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("src/test/resources/data/testdata2.xml"));

	}

	
	protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }
 
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }
 
   
    
    @Test
    public void testDataByName() throws SQLException {
        DataSource dsrc = DataHelper.getDatasource(LIVE_DB);

        Connection con = dsrc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TEST WHERE NAME='James' ");
            assertTrue("ResultSet is empty", results.next());
            assertEquals("Bond", results.getString("VAL"));
            assertEquals("1", results.getString("ID"));
        } finally {
            con.close();
        }
    }

    
    @Test
    public void testDataByVal() throws SQLException {
        DataSource dsrc = DataHelper.getDatasource(LIVE_DB);

        Connection con = dsrc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TEST WHERE VAL='Bond' ");
            assertTrue("ResultSet is empty", results.next());
            assertEquals("James", results.getString("NAME"));
            assertEquals("1", results.getString("ID"));
        } finally {
            con.close();
        }
    }
    
    @Test
    public void testDataById() throws SQLException {
        DataSource dsrc = DataHelper.getDatasource(LIVE_DB);

        Connection con = dsrc.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM TEST WHERE ID='3' ");
            assertTrue("ResultSet is empty", results.next());
            assertEquals("Rafael", results.getString("NAME"));
            assertEquals("Nadal", results.getString("VAL"));
        } finally {
            con.close();
        }
    }
    
    @Test
    public void testTableContent() throws Exception {
    	 try {
	    	// Fetch database data after executing your code
	        IDataSet databaseDataSet = getConnection().createDataSet();
	        ITable actualTable = databaseDataSet.getTable("TEST");
	
	        // Load expected data from an XML dataset
	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/data/assert-this.xml"));
	        ITable expectedTable = expectedDataSet.getTable("TEST");
	
	        // Assert actual database table match expected table
	        Assertion.assertEquals(expectedTable, actualTable);
    	 }catch(Exception ex) {
    		 ex.getStackTrace();
    		 
    	 }
        
    }
    
    
}
