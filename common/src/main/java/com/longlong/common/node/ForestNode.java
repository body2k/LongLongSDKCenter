
package com.longlong.common.node;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 森林节点类
 *
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForestNode extends BaseNode {

	/**
	 * 节点内容
	 */
	private Object content;

	public ForestNode(Integer id, Integer parentId, Object content) {
		this.id = id;
		this.parentId = parentId;
		this.content = content;
	}

}
