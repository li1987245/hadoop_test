package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Baron
 *         使用说明：将转换成树结点的实体实现Entity2TreeInter接口，生成树时，调用getTreeJsonString方法，并传入
 *         List<已实现Entity2TreeInter接口的类>对象即可得到JSON的String
 */
public class Entity2TreeUtils {
	private String id;
	private String text;
	private String cls;
	private boolean leaf;
	private boolean expanded;
	private List<Entity2TreeUtils> children;

	// private boolean checked;

	public Entity2TreeUtils() {
		super();
	}

	public Entity2TreeUtils(String id, String text, String cls, boolean leaf,
							boolean expanded, List<Entity2TreeUtils> children) {
		super();
		this.id = id;
		this.text = text;
		this.cls = cls;
		this.leaf = leaf;
		this.expanded = expanded;
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<Entity2TreeUtils> getChildren() {
		return children;
	}

	public void setChildren(List<Entity2TreeUtils> children) {
		this.children = children;
	}

	public static <T extends Entity2TreeInter> String getTreeJsonString(
			List<T> e2tList) {
		/**
		 * 定义“数组-链表”，该数组链表的每一项相当于一深度为2的小树
		 * Map的key相当于“数组”的某一项，Map的value相当于该key所拥有的“链表”
		 * 这里，key为父节点ID，list为具有相同父节点ID的所有同级子节点实体list（属于该父节点的所有子节点）
		 */
		Map<String, List<Entity2TreeUtils>> arrayListMap = new HashMap<String, List<Entity2TreeUtils>>();

		for (Entity2TreeInter e : e2tList) {
			// 变量定义务必在循环内，对象是引用，不能重复使用同一个对象变量
			Entity2TreeUtils e2t = new Entity2TreeUtils();
			e2t.setId(e.getId());
			e2t.setCls(e.getCls());
			e2t.setText(e.getText());
			e2t.setLeaf(e.getLeaf());
			e2t.setExpanded(e.getExpanded());

			String fatherId = e.getFatherId();
			// 获取当前遍历结点的父ID，并判断该父节点的数组链表项是否存在，如果该“数组项-链表项”不存在，则新建一个，并放入“数组-链表”
			if (arrayListMap.get(fatherId) == null) {
				List<Entity2TreeUtils> list = new ArrayList<Entity2TreeUtils>();
				list.add(e2t);
				arrayListMap.put(fatherId, list);
			} else {
				List<Entity2TreeUtils> valueList = arrayListMap.get(fatherId);
				valueList.add(e2t);
				arrayListMap.put(fatherId, valueList);
			}
		}
		// 以上，至此，第一遍遍历完毕，非叶子节点都拥有一个“数组-链表项”，也即“最小的树”已创建完毕

		// 以下，对“数组链表”Map进行遍历，更改“最小的树”的从属关系（更改指针指向），也即把所有小树组装成大树
		for (Map.Entry<String, List<Entity2TreeUtils>> entry : arrayListMap
				.entrySet()) {
			// 获取当前遍历“数组项-链表项”的链表项，并对链表项进行遍历，从“数组-链表”小树中找到它的子节点，并将该子节点加到该小树的children中
			List<Entity2TreeUtils> smallTreeList = new ArrayList<Entity2TreeUtils>();
			smallTreeList = entry.getValue();
			int nodeListSize = smallTreeList.size();
			for (int i = 0; i < nodeListSize; i++) {
				String findID = smallTreeList.get(i).getId();
				List<Entity2TreeUtils> findList = arrayListMap.get(findID);
				// 以下操作不能取出对象存放在变量中，否则将破坏树的完整性
				smallTreeList.get(i).setChildren(findList);
			}
		}
		// 获取以0为父Id的链表项，该链表项是根节点实体，里面已封装好各子节点，可以由于多个根节点，即这些根结点的父Id都为0
		List<Entity2TreeUtils> rootNodeList = arrayListMap.get("0");

		return JSON.toJSONString(rootNodeList);
	}
}