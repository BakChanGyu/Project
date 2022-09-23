package project.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.member.Member;
import project.missing.MissingMember;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Qualifier("MissingMemberRepository")
public class MissingMemberRepository implements MemberRepository{

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public MissingMember save(MissingMember member) {
        String sql = "insert into missing_list(misscode, name, address, ssn, found_date, found_loc, protector_name, protector_tel, officer_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getMisscode());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getAddress());
            pstmt.setString(4, member.getSsn());
            pstmt.setDate(5, member.getFound_date());
            pstmt.setString(6, member.getFound_loc());
            pstmt.setString(7, member.getProtector_name());
            pstmt.setString(8, member.getProtector_tel());
            pstmt.setLong(9, member.getOfficer_id());

            pstmt.executeUpdate();

            return member;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findLoginId(String loginId) {
        return Optional.empty();
    }

    @Override
    public List<MissingMember> findAll() {
        String sql = "select * from missing_list";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<MissingMember> members = new ArrayList<>();

            while(rs.next()) {
                MissingMember member = new MissingMember();
                member.setName(rs.getString("name"));
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
    public void delete(MissingMember member) {
        String sql = "delete from missing_list where misscode = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMisscode());
            pstmt.execute();

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public MissingMember updateByMissingcode(MissingMember member) {
        String sql = "update missing_list set name = ?, address = ?, ssn = ?, found_date = ?, found_loc = ?, protector_name = ?, protector_tel = ?, officer_id = ? where misscode = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getAddress());
            pstmt.setString(3, member.getSsn());
            pstmt.setDate(4, member.getFound_date());
            pstmt.setString(5, member.getFound_loc());
            pstmt.setString(6, member.getProtector_name());
            pstmt.setString(7, member.getProtector_tel());
            pstmt.setLong(8, member.getOfficer_id());
            pstmt.setString(9, member.getMisscode());

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
