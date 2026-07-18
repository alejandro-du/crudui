package org.vaadin.crudui2.form;

import org.vaadin.crudui2.CrudOperation;

public interface CrudFormFactory<B> {

	CrudForm<B> build(B bean);

	default CrudForm<B> build(B bean, CrudOperation operation) {
		return build(bean);
	}

	/**
	 * Gets the caption to use when creating a new bean.
	 * Default is "Add".
	 *
	 * @return The create caption
	 */
	default String getCreateCaption() {
		return "Add";
	}

	/**
	 * Gets the caption to use when updating an existing bean.
	 * Default is "Update".
	 *
	 * @return The update caption
	 */
	default String getUpdateCaption() {
		return "Update";
	}

}
