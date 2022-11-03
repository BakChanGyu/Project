package project.repository.target;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.target.student.toeic.Toeic;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ToeicRepositoryImpl implements ToeicRepository {

    private final DataSource dataSource;

    @Override
    public Toeic save(Toeic target) {
        String sql = "insert into toeic(toeic_id_code, toeic_name, toeic_ssn, toeic_address, toeic_exam_date, toeic_exam_loc, toeic_register_date)" +
                " values(?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getToeicIdCode());
            pstmt.setString(2, target.getToeicName());
            pstmt.setString(3, target.getToeicSsn());
            pstmt.setString(4, target.getToeicAddress());
            pstmt.setDate(5, Date.valueOf(target.getToeicExamDate()));
            pstmt.setString(6, target.getToeicExamLoc());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Toeic> findAll() {

        String sql = "select * from toeic order by toeic_register_date DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Toeic> targets = new ArrayList<>();

            while(rs.next()) {
                Toeic target = new Toeic();

                target.setToeicIdCode(rs.getString("toeic_id_code"));
                target.setToeicName(rs.getString("toeic_name"));
                target.setToeicSsn(rs.getString("toeic_ssn"));
                target.setToeicAddress(rs.getString("toeic_address"));
                target.setToeicExamDate(rs.getString("toeic_exam_date"));
                target.setToeicExamLoc(rs.getString("toeic_exam_loc"));
                target.setToeicRgstDate(rs.getString("toeic_register_date"));
                target.setToeicUpdateDate(rs.getString("toeic_update_date"));
                target.setToeicIsUploaded(rs.getInt("toeic_is_uploaded"));

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
    public Optional<Toeic> findByIdCode(String toeicIdCode) {

        String sql = "select * from toeic where toeic_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, toeicIdCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Toeic target = new Toeic();
                target.setToeicIdCode(rs.getString("toeic_id_code"));
                target.setToeicName(rs.getString("toeic_name"));
                target.setToeicSsn(rs.getString("toeic_ssn"));
                target.setToeicAddress(rs.getString("toeic_address"));
                target.setToeicExamDate(rs.getString("toeic_exam_date"));
                target.setToeicExamLoc(rs.getString("toeic_exam_loc"));
                target.setToeicRgstDate(rs.getString("toeic_register_date"));
                target.setToeicUpdateDate(rs.getString("toeic_update_date"));
                target.setToeicIsUploaded(rs.getInt("toeic_is_uploaded"));

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
    public String findName(String toeicIdCode) {

        StringBuilder sb = new StringBuilder();
        String sql = "select name from toeic where toeic_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, toeicIdCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sb.append(rs.getString("toeic_name"));
            }

            return String.valueOf(sb);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Toeic update(Toeic target) {

        String sql = "update toeic set toeic_name = ?, toeic_ssn = ?, toeic_address = ?, " +
                "toeic_exam_date = ?, toeic_exam_loc = ?, toeic_update_date = now() where toeic_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, target.getToeicName());
            pstmt.setString(2, target.getToeicSsn());
            pstmt.setString(3, target.getToeicAddress());
            pstmt.setDate(4, Date.valueOf(target.getToeicExamDate()));
            pstmt.setString(5, target.getToeicExamLoc());
            pstmt.setString(6, target.getToeicIdCode());

            pstmt.executeUpdate();

            return target;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void delete(String toeicIdCode) {

        String sql = "delete from toeic where toeic_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, toeicIdCode);
            pstmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void setIsUpdated(String toeicIdCode) {

        String sql = "update toeic set toeic_is_uploaded = 1 where toeic_id_code = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, toeicIdCode);
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

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}