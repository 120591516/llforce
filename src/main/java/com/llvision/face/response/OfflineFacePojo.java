package com.llvision.face.response;

public class OfflineFacePojo {
    private Integer id;

    private String name;

    private Integer status;

    private String create_time;

    private String update_time;
	private String update_time1;
	private String update_time2;

	private String time1;

	private String time2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

	public String getTime1() {
		return time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
	}

	public String getTime2() {
		return time2;
	}

	public void setTime2(String time2) {
		this.time2 = time2;
	}

	public String getUpdate_time1() {
		return update_time1;
	}

	public void setUpdate_time1(String update_time1) {
		this.update_time1 = update_time1;
	}

	public String getUpdate_time2() {
		return update_time2;
	}

	public void setUpdate_time2(String update_time2) {
		this.update_time2 = update_time2;
	}
}