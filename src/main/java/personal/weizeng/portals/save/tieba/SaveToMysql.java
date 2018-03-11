package personal.weizeng.portals.save.tieba;

import personal.weizeng.portals.dto.tieba.TiebaDto;
import personal.weizeng.portals.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */
public class SaveToMysql {

    public static void save2MySql(String query, Connection connection, List<TiebaDto> tiebaDtos) {

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            //uuid,tieba_name,crawl_date,super_category,low_category,url,focus,post_total,post_superior,pic_num,groups,group_member
            for (TiebaDto tiebaDto : tiebaDtos) {
                stmt.setString(1, tiebaDto.getUUId());
                stmt.setString(2, tiebaDto.getName());
                stmt.setDate(3, new Date(tiebaDto.getDate()));
                stmt.setString(4, tiebaDto.getSuperCategory());
                stmt.setString(5, tiebaDto.getLowCategory());
                stmt.setString(6, tiebaDto.getUrl());
                stmt.setInt(7, tiebaDto.getFoucs());
                stmt.setInt(8, tiebaDto.getPostTotal());
                stmt.setInt(9, tiebaDto.getPostSuperior());
                stmt.setInt(10, tiebaDto.getPicNum());
                stmt.setInt(11, tiebaDto.getGroups());
                stmt.setInt(12, tiebaDto.getGroupMenber());
                stmt.addBatch();
            }
            stmt.executeBatch();
            connection.commit();
            System.out.println("save");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
