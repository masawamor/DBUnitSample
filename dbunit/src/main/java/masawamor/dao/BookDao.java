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
		
		String sql = "INSERT INTO (id, title, authorId) VALUES (?, ?, ?);";
		
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
	
	public Book selectOne(int id) throws Exception {
		
		String sql = "SELECT id, title, authorId FROM books WHERE id = ?";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		pstmt.setInt(1, id);
		
		ResultSet rs = pstmt.executeQuery();
		
		List<Book> books = new ArrayList<Book>();
		
		rs.next();
		
		Book book = new Book();
		book.setId(rs.getInt("id"));
		book.setTitle(rs.getString("title"));
		
		return book;
	}
	
	public List<Book> selectAll() throws Exception {
		
		String sql = "SELECT id, title, authorId FROM books";

		PreparedStatement pstmt = this.conn.prepareStatement(sql);
		
		ResultSet rs = pstmt.executeQuery();
		
		List<Book> books = new ArrayList<Book>();
		
		while (rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("id"));
			book.setTitle(rs.getString("title"));
			
			books.add(book);
		}
		
		return books;
	}
}
