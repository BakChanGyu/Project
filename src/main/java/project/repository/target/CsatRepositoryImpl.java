package project.repository.target;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.target.student.csat.Csat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CsatRepositoryImpl implements CsatRepository {

    private final DataSource dataSource;

    @Override
    public Csat save(Csat target) {
        String sql = "insert into csat(id_code, name, ssn, address, exam_date, exam_loc, register_date) values(?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getIdCode());
            pstmt.setString(2, target.getName());
            pstmt.setString(3, target.getSsn());
            pstmt.setString(4, target.getAddress());
            pstmt.setDate(5, target.getExamDate());
            pstmt.setString(6, target.getExamLoc());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Csat> findAll() {

        String sql = "select * from csat";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Csat> targets = new ArrayList<>();

            while(rs.next()) {
                Csat target = new Csat();

                target.setIdCode(rs.getString("id_code"));
                target.setName(rs.getString("name"));
                target.setSsn(rs.getString("ssn"));
                target.setAddress(rs.getString("address"));
                target.setExamDate(rs.getDate("exam_date"));
                target.setExamLoc(rs.getString("exam_loc"));
                target.setRgstDate(rs.getDate("register_date"));
                target.setUpdateDate(rs.getDate("update_date"));

                targets.add(target);
            }

            return targets;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Csat> findByIdCode(String idCode) {

        String sql = "select * from csat where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Csat target = new Csat();
                target.setIdCode(rs.getString("id_code"));
                target.setName(rs.getString("name"));
                target.setSsn(rs.getString("ssn"));
                target.setAddress(rs.getString("address"));
                target.setExamDate(rs.getDate("exam_date"));
                target.setExamLoc(rs.getString("exam_loc"));
                target.setRgstDate(rs.getDate("register_date"));
                target.setUpdateDate(rs.getDate("update_date"));

                return Optional.of(target);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public String findName(String idCode) {

        StringBuilder sb = new StringBuilder();
        String sql = "select name from csat where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, idCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sb.append(rs.getString("name"));
            }

            return String.valueOf(sb);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Csat update(Csat target) {

        String sql = "update csat set name = ?, ssn = ?, address = ?, exam_date = ?, exam_loc = ?, update_date = now() where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getName());
            pstmt.setString(2, target.getSsn());
            pstmt.setString(3, target.getAddress());
            pstmt.setDate(4, target.getExamDate());
            pstmt.setString(5, target.getExamLoc());
            pstmt.setString(6, target.getIdCode());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void delete(String idCode) {

        String sql = "delete from csat where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idCode);
            pstmt.execute();

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

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
