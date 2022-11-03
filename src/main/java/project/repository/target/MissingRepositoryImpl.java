package project.repository.target;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.target.missing.Missing;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MissingRepositoryImpl implements MissingRepository {

    private final DataSource dataSource;

    @Override
    public Missing save(Missing target) {

        String sql = "insert into missing(missing_id_code, missing_name, missing_ssn, missing_address, missing_date, protector_name, protector_tel, missing_register_date) values(?, ?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getMissingIdCode());
            pstmt.setString(2, target.getMissingName());
            pstmt.setString(3, target.getMissingSsn());
            pstmt.setString(4, target.getMissingAddress());
            pstmt.setDate(5, Date.valueOf(target.getMissingDate()));
            pstmt.setString(6, target.getProtectorName());
            pstmt.setString(7, target.getProtectorTel());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Missing> findAll() {

        String sql = "select * from missing order by missing_register_date DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Missing> targets = new ArrayList<>();

            while(rs.next()) {
                Missing target = new Missing();

                target.setMissingIdCode(rs.getString("missing_id_code"));
                target.setMissingName(rs.getString("missing_name"));
                target.setMissingSsn(rs.getString("missing_ssn"));
                target.setMissingAddress(rs.getString("missing_address"));
                target.setMissingDate(rs.getString("missing_date"));
                target.setProtectorName(rs.getString("protector_name"));
                target.setProtectorTel(rs.getString("protector_tel"));
                target.setMissingRgstDate(rs.getString("missing_register_date"));
                target.setMissingUpdateDate(rs.getString("missing_update_date"));
                target.setMissingIsUploaded(rs.getInt("missing_is_uploaded"));

                targets.add(target);
            }

            return targets;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 개인 코드로 조회
    @Override
    public Optional<Missing> findByIdCode(String missingIdCode) {

        String sql = "select * from missing where missing_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, missingIdCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Missing target = new Missing();
                target.setMissingIdCode(rs.getString("missing_id_code"));
                target.setMissingName(rs.getString("missing_name"));
                target.setMissingSsn(rs.getString("missing_ssn"));
                target.setMissingAddress(rs.getString("missing_address"));
                target.setMissingDate(String.valueOf(rs.getDate("missing_date")));
                target.setProtectorName(rs.getString("protector_name"));
                target.setProtectorTel(rs.getString("protector_tel"));
                target.setMissingRgstDate(rs.getString("missing_register_date"));
                target.setMissingUpdateDate(rs.getString("missing_update_date"));
                target.setMissingIsUploaded(rs.getInt("missing_is_uploaded"));

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
    public String findName(String missingIdCode) {

        StringBuilder sb = new StringBuilder();
        String sql = "select missing_name from missing where missing_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, missingIdCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sb.append(rs.getString("missing_name"));
            }

            return String.valueOf(sb);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Missing update(Missing target) {

        String sql = "update missing set missing_name = ?, missing_ssn = ?, missing_address = ?, missing_date = ?, protector_name = ?, protector_tel = ?, missing_update_date = now() where missing_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getMissingName());
            pstmt.setString(2, target.getMissingSsn());
            pstmt.setString(3, target.getMissingAddress());
            pstmt.setDate(4, Date.valueOf(target.getMissingDate()));
            pstmt.setString(5, target.getProtectorName());
            pstmt.setString(6, target.getProtectorTel());
            pstmt.setString(7, target.getMissingIdCode());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void delete(String missingIdCode) {

        String sql = "delete from missing where missing_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, missingIdCode);
            pstmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void setIsUpdated(String missingIdCode) {

        String sql = "update missing set missing_is_uploaded = 1 where missing_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, missingIdCode);
            pstmt.executeUpdate();

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
