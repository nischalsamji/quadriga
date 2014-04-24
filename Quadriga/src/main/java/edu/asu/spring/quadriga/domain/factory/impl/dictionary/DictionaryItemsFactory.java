package edu.asu.spring.quadriga.domain.factory.impl.dictionary;

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

	@Override
	public IDictionaryItems cloneDictionaryItemsObject(
			IDictionaryItems dictionaryItems) 
	{
		IDictionaryItems clone = createDictionaryItemsObject();
		clone.setDictionary(dictionaryItems.getDictionary());
		clone.setDictionaryItem(dictionaryItems.getDictionaryItem());
		clone.setCreatedBy(dictionaryItems.getCreatedBy());
		clone.setCreatedDate(dictionaryItems.getCreatedDate());
		clone.setUpdatedBy(dictionaryItems.getUpdatedBy());
		clone.setUpdatedDate(dictionaryItems.getUpdatedDate());
		return clone;
	}
	
	


}
