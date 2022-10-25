package masawamor.dao;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
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
        //RunScript.execute(URL, USER, PASSWORD, "src/test/resources/schema.sql", Charset.forName("UTF-8"), false);
        // UTF8の定数がなぜかないのでベタで、Charset.forName("UTF-8")
        // https://jar-download.com/artifacts/com.h2database/h2/1.4.189/source-code/org/h2/engine/Constants.java
	}
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, URL, USER, PASSWORD);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		connection = databaseTester.getConnection();
		
		RunScript.execute(URL, USER, PASSWORD, "src/test/resources/schema.sql", Charset.forName("UTF-8"), false);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		if (connection != null) {
			connection.close();
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		// DBにデータ投入
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		IDataSet dataSet = builder.build(new File("src/test/resources/BookDao/actual.xml"));
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
		Book book = new BookDao(conn).findById(2);
		System.out.println("@2");
	}
	
//	private XmlDataSet readXmlDataSet(String path) throws Exception {
//		try (InputStream istream = getClass().getResourceAsStream(path)) {
//			return new XmlDataSet(istream);
//		}
//	}

}
