package masawamor.dao;

import java.io.InputStream;
import java.sql.Connection;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import masawamor.entity.Book;

public class BookDaoTest {

	static IDatabaseTester databaseTester;
	static IDatabaseConnection connection;

	private static final String JDBC_DRIVER = "org.h2.Driver";
	private static final String URL = "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";
	
	@BeforeClass
    public static void createSchema() throws Exception {
        RunScript.execute(URL, USER, PASSWORD, "resources/schema.sql", null, false);
    }
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, URL, USER, PASSWORD);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		connection = databaseTester.getConnection();
		
//		PreparedStatement pstmt = connection.getConnection().prepareStatement(
//				"CREATE TABLE books (id int, title varchar, authorid int);"
//				);
//		pstmt.execute();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		if (connection != null) {
			connection.close();
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		XmlDataSet dataSet = readXmlDataSet("resources/BookDao/actual.xml");
		//FlatXmlDataSet dataSet = new FlatXmlDataSet(new FileInputStream("resources/BookDaoTset/actual.xml"));
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		System.out.println("@1");
		Connection conn = connection.getConnection();
		Book book = new BookDao(conn).selectOne(1);
		System.out.println("@2");
	}
	
	private XmlDataSet readXmlDataSet(String path) throws Exception {
		try (InputStream istream = getClass().getResourceAsStream(path)) {
			return new XmlDataSet(istream);
		}
	}

}
