package personal.weizeng.portals.dto.tieba;

/**
 * Created by Administrator on 2018/3/7.
 */
public class CategoryDto {

    private String superCategory;
    private String lowCategory;
    private String lowCategoryURL;

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }

    public String getLowCategory() {
        return lowCategory;
    }

    public void setLowCategory(String lowCategory) {
        this.lowCategory = lowCategory;
    }

    public String getLowCategoryURL() {
        return lowCategoryURL;
    }

    public void setLowCategoryURL(String lowCategoryURL) {
        this.lowCategoryURL = lowCategoryURL;
    }
}
