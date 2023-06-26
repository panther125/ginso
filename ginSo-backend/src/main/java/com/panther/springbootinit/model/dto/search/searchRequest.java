package com.panther.springbootinit.model.dto.search;

import com.panther.springbootinit.common.PageRequest;
import lombok.Data;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 18:54
 */
@Data
public class searchRequest extends PageRequest {

    private String type;

    private String searchText;

    private static final long serialVersionUID = 1L;
}
