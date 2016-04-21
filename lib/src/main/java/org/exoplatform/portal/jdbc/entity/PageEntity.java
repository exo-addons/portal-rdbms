package org.exoplatform.portal.jdbc.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.exoplatform.commons.api.persistence.ExoEntity;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.SiteType;
import org.exoplatform.portal.mop.page.PageContext;
import org.exoplatform.portal.mop.page.PageKey;
import org.exoplatform.portal.mop.page.PageState;

@Entity
@ExoEntity
@Table(name = "PORTAL_PAGES")
@NamedQueries({
    @NamedQuery(name = "PageEntity.findByKey", query = "SELECT p FROM PageEntity p WHERE p.ownerType = :ownerType AND p.ownerId = :ownerId AND p.name = :name") })
public class PageEntity extends ComponentEntity implements Serializable {

  private static final long serialVersionUID = -6195451978995765259L;

  @Column(name = "OWNER_TYPE")
  private SiteType          ownerType;

  @Column(name = "OWNER_ID", length = 200)
  private String            ownerId;

  @Column(name = "SHOW_MAX_WINDOW")
  private boolean           showMaxWindow;

  @Column(name = "DISPLAY_NAME", length = 200)
  private String            displayName;
  
  @Column(name = "NAME", length = 200)
  private String                name;
  
  @Column(name = "DESCRIPTION", length = 2000)
  private String                description;
  
  @Column(name = "FACTORY_ID", length = 200)
  private String                factoryId;

  @Column(name = "EDIT_PERMISSION", length = 500)
  private String            editPermission;

  @Column(name = "PAGE_BODY", length = 5000)
  private String            pageBody = new JSONArray().toJSONString();
  
  @Column(name = "MOVE_APP_PERMISSION", length = 2000)
  private String                moveAppsPermissions;

  @Column(name = "MOVE_CONTAINER_PERMISSION", length = 2000)
  private String                moveContainersPermissions;
  
  @Transient
  private List<ComponentEntity> children         = new LinkedList<ComponentEntity>();
  
  public SiteType getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(SiteType ownerType) {
    this.ownerType = ownerType;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getEditPermission() {
    return editPermission;
  }

  public void setEditPermission(String editPermission) {
    this.editPermission = editPermission;
  }

  public boolean isShowMaxWindow() {
    return showMaxWindow;
  }

  public void setShowMaxWindow(boolean showMaxWindow) {
    this.showMaxWindow = showMaxWindow;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getPageBody() {
    return pageBody;
  }

  public void setPageBody(String pageBody) {
    this.pageBody = pageBody;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFactoryId() {
    return factoryId;
  }

  public void setFactoryId(String factoryId) {
    this.factoryId = factoryId;
  }

  public String getMoveAppsPermissions() {
    return moveAppsPermissions;
  }

  public void setMoveAppsPermissions(String moveAppsPermissions) {
    this.moveAppsPermissions = moveAppsPermissions;
  }

  public String getMoveContainersPermissions() {
    return moveContainersPermissions;
  }

  public void setMoveContainersPermissions(String moveContainersPermissions) {
    this.moveContainersPermissions = moveContainersPermissions;
  }

  public PageContext buildPageContext() {
    PageState state = new PageState(getDisplayName(),
                                    getDescription(),
                                    isShowMaxWindow(),
                                    getFactoryId(),
                                    convert(getAccessPermissions()),
                                    getEditPermission(),
                                    convert(getMoveAppsPermissions()),
                                    convert(getMoveContainersPermissions()));

    SiteKey siteKey = new SiteKey(getOwnerType(), getOwnerId());
    PageKey pageKey = new PageKey(siteKey, getName());

    PageContext context = new PageContext(pageKey, state);
    return context;
  }
  
  public List<ComponentEntity> getChildren() {
    return children;
  }

  public void setChildren(List<ComponentEntity> children) {
    this.children = children;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject obj = super.toJSON();

    JSONArray jChildren = new JSONArray();
    for (ComponentEntity child : getChildren()) {
      jChildren.add(child.toJSON());
    }
    obj.put("children", jChildren);

    return obj;
  }

  @Override
  public TYPE getType() {
    return TYPE.PAGE;
  }

}
