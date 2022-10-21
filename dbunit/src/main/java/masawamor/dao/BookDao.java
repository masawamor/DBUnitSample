package masawamor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import masawamor.entity.Book;

public class BookDao {
	
	private final Connection conn;
	
	public BookDao(Connection conn) {
		this.conn = conn;
	}

	public boolean insert(Book book) throws Exception {
		
		String sql = "INSERT INTO (id, title) VALUES (?, ?, ?);";
		
		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, book.getId());
		pstmt.setString(2, book.getTitle());
		
		boolean result;
		try {
			result = pstmt.execute();
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			result = false;
		}
		
		return result;
		
	}
	
	public Book findById(int id) throws Exception {
		
		String sql = "SELECT id, title FROM books WHERE id = ?";
		
		// https://www.codejava.net/java-se/jdbc/how-to-use-scrollable-result-sets-with-jdbc
		PreparedStatement pstmt = this.conn.prepareStatement(
				sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		pstmt.setInt(1, id);
		
		ResultSet rs = pstmt.executeQuery();
		
		List<Book> books = new ArrayList<Book>();
		
		if (getCountResult(rs) == 0) {
			return null;
		}
		
		Book book = new Book();
		book.setId(rs.getInt("id"));
		book.setTitle(rs.getString("title"));
		
		return book;
	}
	
	public List<Book> selectAll() throws Exception {
		
		String sql = "SELECT id, title FROM books";

		PreparedStatement pstmt = this.conn.prepareStatement(
				sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = pstmt.executeQuery();
		
		List<Book> books = new ArrayList<Book>();
		
		if (getCountResult(rs) == 0) {
			return books;
		}
		
		while (rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("id"));
			book.setTitle(rs.getString("title"));
			
			books.add(book);
		}
		
		return books;
	}
	
	private int getCountResult(ResultSet rs) throws Exception {
		rs.last();
		int count = rs.getRow();
		rs.first();
		return count;
	}
}
