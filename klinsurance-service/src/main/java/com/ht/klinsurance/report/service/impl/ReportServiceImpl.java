package com.ht.klinsurance.report.service.impl;

import com.ht.klinsurance.report.mapper.ReportBriefingMapper;
import com.ht.klinsurance.report.mapper.ReportMapper;
import com.ht.klinsurance.report.model.Report;
import com.ht.klinsurance.report.model.ReportBriefing;
import com.ht.klinsurance.report.service.IBuildReportService;
import com.ht.klinsurance.report.service.IReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements IReportService {

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private ReportBriefingMapper reportBriefingMapper;

    @Resource
    private IBuildReportService buildReportService;
    /**
     * 创建报告
     *
     * @param report
     * @param reportBriefingList
     * @return
     */
    @Override
    public String  creataReport(Report report, List<ReportBriefing> reportBriefingList,String basePath)
    {
       try {
           int result = 1;
           //首先删除该报告的已存在内容
           reportMapper.deleteById(report.getReportId());
           result*=reportMapper.addReport(report);
           //保存ReportBriefing  也是先删除再保存
           reportBriefingMapper.deleteByReportId(report.getReportId());
           if(reportBriefingList!=null)
           {
               for(int i = 0;i<reportBriefingList.size();i++)
               {
                   result*= reportBriefingMapper.addReportBriefing(reportBriefingList.get(i));
               }
           }
           String ftpUrl=buildReportService.buildReport(basePath,report.getReportId());
           return ftpUrl;
       }catch (Exception e)
       {
           return  null;
       }
    }
}
