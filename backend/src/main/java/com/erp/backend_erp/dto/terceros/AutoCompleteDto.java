package com.erp.backend_erp.dto.terceros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoCompleteDto<T> {

	private String search;

	private T params;
}
