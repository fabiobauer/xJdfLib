/**
 * All rights reserved by
 * 
 * flyeralarm GmbH
 * Alfred-Nobel-Straße 18
 * 97080 Würzburg
 *
 * info@flyeralarm.com
 * http://www.flyeralarm.com
 */
package org.cip4.lib.xjdf.builder;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.ValidationException;
import javax.xml.namespace.QName;

import org.cip4.lib.xjdf.XJdfNodeFactory;
import org.cip4.lib.xjdf.schema.ChildProduct;
import org.cip4.lib.xjdf.schema.Intent;
import org.cip4.lib.xjdf.schema.IntentType;
import org.cip4.lib.xjdf.schema.Product;
import org.cip4.lib.xjdf.util.IDGeneratorUtil;
import org.cip4.lib.xjdf.xml.XJdfConstants;

/**
 * Implementation of a Product builder class.
 * @author s.meissner
 * @date 05.03.2012
 */
public class ProductBuilder extends AbstractNodeBuilder<Product> {

	private static final String ID_PREFIX = "PRD";

	private final XJdfNodeFactory xJdfNodeFactory;

	/**
	 * Default constructor.
	 */
	public ProductBuilder() {

		this(null, null, null, null);
	}

	/**
	 * Custom Constructor. Creates a new instance of ProductBuilder which already contains attribute Amount.
	 * @param amount Value of Amount attribute
	 */
	public ProductBuilder(Integer amount) {

		this(amount, null, null, null);
	}

	/**
	 * Custom Constructor. Creates a new instance of ProductBuilder which already contains attributes Amount, ID, ProductType and ProductTypeDetails.
	 * @param amount Value of Amount attribute
	 * @param productType Value of ProductType attribute
	 * @param productTypeDetails Value of ProductTypeDetails attribute
	 */
	public ProductBuilder(Integer amount, String productType, String productTypeDetails) {

		this(amount, productType, productTypeDetails, null);
	}

	/**
	 * Custom Constructor. Creates a new instance of ProductBuilder which already contains attributes Amount, ID, ProductType and ProductTypeDetails.
	 * @param amount Value of Amount attribute
	 * @param id Value of ID attribute
	 * @param productType Value of ProductType attribute
	 * @param productTypeDetails Value of ProductTypeDetails attribute
	 * @param descriptiveName String value for DescriptiveName attribute.
	 */
	public ProductBuilder(Integer amount, String productType, String productTypeDetails, String descriptiveName) {

		// initialize objects
		super(new XJdfNodeFactory().createProduct());
		xJdfNodeFactory = new XJdfNodeFactory();

		// set attributes
		getProduct().setAmount(amount);
		getProduct().setProductType(productType);
		getProduct().setProductTypeDetails(productTypeDetails);
		getProduct().setDescriptiveName(descriptiveName);
	}

	/**
	 * Getter for product attribute.
	 * @return the product
	 */
	protected Product getProduct() {
		return getNode();
	}

	/**
	 * Append Intent node to Product Definition.
	 * @param intent Intent object to append to.
	 * @return The current ProductBuilder instance.
	 */
	public ProductBuilder addIntent(IntentType intent) {

		if (intent == null)
			return this;

		// get parameter name
		String intentName = intent.getClass().getSimpleName();

		// create intent node
		Intent it = xJdfNodeFactory.createIntent();
		it.setName(intentName);

		QName qname = new QName(XJdfConstants.NAMESPACE_JDF20, intentName);
		JAXBElement obj = new JAXBElement(qname, intent.getClass(), null, intent);
		it.getIntentType().add(obj);

		// append intent to product
		getProduct().getIntent().add(it);

		// return current builder
		return this;
	}

	/**
	 * Append another product as child.
	 * @param intent Intent object to append to.
	 * @return The current ProductBuilder instance.
	 * @throws ValidationException
	 */
	public ProductBuilder addChildProduct(Product product) throws ValidationException {

		// if necessary, create root ID
		if (getNode().getID() == null || getNode().getID().equals("")) {
			getNode().setID(IDGeneratorUtil.generateID(ID_PREFIX));
		}

		// if neccessary, create child ID
		if (product.getID() == null || product.getID().equals("")) {
			product.setID(IDGeneratorUtil.generateID(ID_PREFIX));
		}

		// create child product
		ChildProduct childProduct = xJdfNodeFactory.createChildProduct();
		childProduct.setChildRef(product);
		getProduct().getChildProduct().add(childProduct);

		// set root flag
		getProduct().setIsRoot(true);

		// validation
		if (product.getChildProduct().size() > 0) {
			throw new ValidationException("A Child Product cannot have children!");
		}

		// return current builder
		return this;
	}

}
