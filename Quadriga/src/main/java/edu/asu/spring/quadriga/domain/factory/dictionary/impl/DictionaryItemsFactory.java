package edu.asu.spring.quadriga.domain.factory.dictionary.impl;

import org.springframework.stereotype.Service;

import edu.asu.spring.quadriga.domain.dictionary.IDictionaryItems;
import edu.asu.spring.quadriga.domain.factory.dictionary.IDictionaryItemsFactory;
import edu.asu.spring.quadriga.domain.impl.dictionary.DictionaryItems;

@Service
public class DictionaryItemsFactory implements IDictionaryItemsFactory {


	/**
	 * {@inheritDoc}
	*/
	@Override
	public IDictionaryItems createDictionaryItemsObject() {

		return new DictionaryItems();
	}


}
