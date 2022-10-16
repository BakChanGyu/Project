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

        String sql = "insert into missing(id_code, name, ssn, address, missing_date, protector_name, protector_tel, register_date) values(?, ?, ?, ?, ?, ?, ?, now())";

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

        String sql = "select * from missing";

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

                target.setIdCode(rs.getString("id_code"));
                target.setName(rs.getString("name"));
                target.setSsn(rs.getString("ssn"));
                target.setAddress(rs.getString("address"));
                target.setMissingDate(rs.getString("missing_date"));
                target.setProtectorName(rs.getString("protector_name"));
                target.setProtectorTel(rs.getString("protector_tel"));
                target.setRgstDate(rs.getString("register_date"));
                target.setUpdateDate(rs.getString("update_date"));

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
    public Optional<Missing> findByIdCode(String idCode) {

        String sql = "select * from missing where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Missing target = new Missing();
                target.setIdCode(rs.getString("id_code"));
                target.setName(rs.getString("name"));
                target.setSsn(rs.getString("ssn"));
                target.setAddress(rs.getString("address"));
                target.setMissingDate(String.valueOf(rs.getDate("missing_date")));
                target.setProtectorName(rs.getString("protector_name"));
                target.setProtectorTel(rs.getString("protector_tel"));
                target.setRgstDate(rs.getString("register_date"));
                target.setUpdateDate(rs.getString("update_date"));

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
        String sql = "select name from missing where id_code = ?";

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
    public Missing update(Missing target) {

        String sql = "update missing set name = ?, ssn = ?, address = ?, missing_date = ?, protector_name = ?, protector_tel = ?, update_date = now() where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getName());
            pstmt.setString(2, target.getSsn());
            pstmt.setString(3, target.getAddress());
            pstmt.setDate(4, Date.valueOf(target.getMissingDate()));
            pstmt.setString(5, target.getProtectorName());
            pstmt.setString(6, target.getProtectorTel());
            pstmt.setString(7, target.getIdCode());

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

        String sql = "delete from missing where id_code = ?";

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

    private void close(Connection conn) throws SQLException{

        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
