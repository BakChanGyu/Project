package project.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.identification.IdentificationTarget;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class IdentificationRepositoryImpl implements IdentificationRepository {

    private final DataSource dataSource;

    @Override
    public IdentificationTarget save(IdentificationTarget member) {
        String sql = "insert into identification_target(id_code, name, rgst_date, member_id) values(?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getIdCode());
            pstmt.setString(2, member.getName());
            pstmt.setDate(3, member.getRgstDate());
            pstmt.setLong(4, member.getMemberId());

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<IdentificationTarget> findAll() {
        String sql = "select * from identification_target";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<IdentificationTarget> members = new ArrayList<>();

            while(rs.next()) {
                IdentificationTarget member = new IdentificationTarget();
                member.setIdCode(rs.getString("id_code"));
                member.setName(rs.getString("name"));
                member.setRgstDate(rs.getDate("rgst_date"));
                member.setMemberId(rs.getLong("member_id"));

                members.add(member);
            }

            return members;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public int countId(Long id) {
        return 0;
    }

    @Override
    public int countLoginID(String loginId) {
        return 0;
    }

    @Override
    public void delete(IdentificationTarget member) {
        String sql = "delete from identification_target where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getIdCode());
            pstmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public IdentificationTarget updateByMissingcode(IdentificationTarget member) {
        String sql = "update identification_target set name = ?, rgst_date = ?, member_id = ? where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getName());
            pstmt.setDate(2, member.getRgstDate());
            pstmt.setLong(3, member.getMemberId());
            pstmt.setString(4, member.getIdCode());

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException{
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
