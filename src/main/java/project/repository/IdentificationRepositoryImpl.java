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
    public IdentificationTarget save(IdentificationTarget target) {
        String sql = "insert into identification_target(id_code, name, ssn, address, rgst_date) values(?, ?, ?, ?, now())";

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

            pstmt.executeUpdate();

            return target;
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

            List<IdentificationTarget> targets = new ArrayList<>();

            while(rs.next()) {
                IdentificationTarget target = new IdentificationTarget();

                target.setIdCode(rs.getString("id_code"));
                target.setName(rs.getString("name"));
                target.setSsn(rs.getString("ssn"));
                target.setSsn(rs.getString("address"));
                target.setRgstDate(rs.getDate("rgst_date"));
                target.setMemberId(rs.getLong("member_id"));

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
    public IdentificationTarget updateTable(IdentificationTarget target) {
        String sql = "update identification_target set name = ?, ssn = ?, address = ?, update_date = now() where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getName());
            pstmt.setString(2, target.getSsn());
            pstmt.setString(3, target.getAddress());
            pstmt.setString(4, target.getIdCode());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void delete(IdentificationTarget target) {
        String sql = "delete from identification_target where id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, target.getIdCode());
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
