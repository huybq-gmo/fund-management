package com.huybq.fund_management.domain.fund;

import com.huybq.fund_management.domain.contributions.Contribution;
import com.huybq.fund_management.domain.contributions.ContributionRepository;
import com.huybq.fund_management.domain.period.Period;
import com.huybq.fund_management.domain.period.PeriodRepository;
import com.huybq.fund_management.domain.period.PeriodService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class FundService {
    private final FundRepository fundRepository;
    private final PeriodService periodService;

    public Fund createFund(FundDTO fundDTO) {
        // Tạo Fund mới
        Fund fund = new Fund();
        fund.setName(fundDTO.name());
        fund.setDescription(fundDTO.description());
        fund.setType(FundType.valueOf(fundDTO.type()));
        fund.setAmount(fundDTO.amount());
        // Lưu vào database
        return fundRepository.save(fund);
    }

    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    public Fund getFundById(Integer id) {
        return fundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fund not found with ID: " + id));
    }

    public Fund updateFund(Integer id, FundDTO fundDTO) {
        Fund updatedFund = fundRepository.findById(id)
                .map(existingFund -> {
                    existingFund.setName(fundDTO.name());
                    existingFund.setDescription(fundDTO.description());
                    existingFund.setType(FundType.valueOf(fundDTO.type()));
                    existingFund.setAmount(fundDTO.amount());
                    return fundRepository.save(existingFund);
                })
                .orElseThrow(() -> new EntityNotFoundException("Fund not found with ID: " + id));

        LocalDate currentDate = LocalDate.now();
        var period = periodService.getPeriodByMonthAndYear(currentDate.getMonthValue(), currentDate.getYear());
        period.setTotalAmount(periodService.calculateTotalAmount());
        periodService.savePeriod(period);

        return updatedFund;
    }

    public void deleteFund(Integer id) {
        if (!fundRepository.existsById(id)) {
            throw new EntityNotFoundException("Fund not found with ID: " + id);
        }
        fundRepository.deleteById(id);
    }
}
