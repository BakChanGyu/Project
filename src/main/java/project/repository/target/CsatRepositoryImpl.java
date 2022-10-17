package project.repository.target;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.target.student.csat.Csat;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CsatRepositoryImpl implements CsatRepository {

    private final DataSource dataSource;

    @Override
    public Csat save(Csat target) {
        String sql = "insert into csat(csat_id_code, csat_name, csat_ssn, csat_address, csat_exam_date, csat_exam_loc, csat_register_date)" +
                " values(?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getCsatIdCode());
            pstmt.setString(2, target.getCsatName());
            pstmt.setString(3, target.getCsatSsn());
            pstmt.setString(4, target.getCsatAddress());
            pstmt.setDate(5, Date.valueOf(target.getCsatExamDate()));
            pstmt.setString(6, target.getCsatExamLoc());

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

                target.setCsatIdCode(rs.getString("csat_id_code"));
                target.setCsatName(rs.getString("csat_csat_name"));
                target.setCsatSsn(rs.getString("ssn"));
                target.setCsatAddress(rs.getString("csat_address"));
                target.setCsatExamDate(rs.getString("csat_exam_date"));
                target.setCsatExamLoc(rs.getString("csat_exam_loc"));
                target.setCsatRgstDate(rs.getString("csat_register_date"));
                target.setCsatUpdateDate(rs.getString("csat_update_date"));

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
    public Optional<Csat> findByIdCode(String csatIdCode) {

        String sql = "select * from csat where csat_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, csatIdCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Csat target = new Csat();
                target.setCsatIdCode(rs.getString("csat_id_code"));
                target.setCsatName(rs.getString("csat_name"));
                target.setCsatSsn(rs.getString("csat_ssn"));
                target.setCsatAddress(rs.getString("csat_address"));
                target.setCsatExamDate(rs.getString("csat_exam_date"));
                target.setCsatExamLoc(rs.getString("csat_exam_loc"));
                target.setCsatRgstDate(rs.getString("csat_register_date"));
                target.setCsatUpdateDate(rs.getString("csat_update_date"));

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
    public String findName(String csatIdCode) {

        StringBuilder sb = new StringBuilder();
        String sql = "select name from csat where csat_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, csatIdCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sb.append(rs.getString("csat_name"));
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

        String sql = "update csat set csat_name = ?, csat_ssn = ?, csat_address = ?, " +
                "csat_exam_date = ?, csat_exam_loc = ?, csat_update_date = now() where csat_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getCsatName());
            pstmt.setString(2, target.getCsatSsn());
            pstmt.setString(3, target.getCsatAddress());
            pstmt.setDate(4, Date.valueOf(target.getCsatExamDate()));
            pstmt.setString(5, target.getCsatExamLoc());
            pstmt.setString(6, target.getCsatIdCode());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void delete(String csatIdCode) {

        String sql = "delete from csat where csat_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, csatIdCode);
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
