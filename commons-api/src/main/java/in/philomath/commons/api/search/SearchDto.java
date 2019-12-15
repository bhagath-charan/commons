package in.philomath.commons.api.search;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

/**
 * created by Bhagath Charan on 15-12-2019
 */
@Data
@NoArgsConstructor
public class SearchDto {

	private String key;

	private String type;

	private String searchType;

	private String value;

	private ArrayList<String> values;

	private Date from;

	private Date to;
}
