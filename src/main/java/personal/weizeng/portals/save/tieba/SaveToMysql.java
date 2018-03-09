package personal.weizeng.portals.save.tieba;

import personal.weizeng.portals.dto.tieba.TiebaDto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/9.
 */
public class SaveToMysql {

    public static void save2MySql(String query, Connection connection, ArrayList<TiebaDto> tiebaDtos) {

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            //uuid,tieba_name,crawl_date,super_category,low_category,url,focus,post_total,post_superior,pic_num,groups,group_member
            for (TiebaDto tiebaDto : tiebaDtos) {
                stmt.setString(1, tiebaDto.getUUId());
                stmt.setDate(2, new Date(tiebaDto.getDate()));
                stmt.setString(3, tiebaDto.getSuperCategory());
                stmt.setString(4, tiebaDto.getLowCategory());
                stmt.setString(5, tiebaDto.getUrl());
                stmt.setInt(6, tiebaDto.getFoucs());
                stmt.setInt(7, tiebaDto.getPostTotal());
                stmt.setInt(8, tiebaDto.getPostSuperior());
                stmt.setInt(9, tiebaDto.getPicNum());
                stmt.setInt(10, tiebaDto.getGroups());
                stmt.setInt(11, tiebaDto.getGroupMenber());
                stmt.addBatch();
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
