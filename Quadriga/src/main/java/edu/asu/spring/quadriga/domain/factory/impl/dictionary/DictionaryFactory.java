package edu.asu.spring.quadriga.domain.factory.impl.dictionary;

import org.springframework.stereotype.Service;

import edu.asu.spring.quadriga.domain.dictionary.IDictionary;
import edu.asu.spring.quadriga.domain.factory.dictionary.IDictionaryFactory;
import edu.asu.spring.quadriga.domain.impl.dictionary.Dictionary;

/**
 * Factory class for creating {@link Dictionary}.
 * 
 * @author Lohith Dwaraka
 *
 */
@Service
public class DictionaryFactory implements IDictionaryFactory {
	@Override
	public IDictionary createDictionaryObject() {
		
		return new Dictionary();
	}

}