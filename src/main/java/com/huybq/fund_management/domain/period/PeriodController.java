package com.huybq.fund_management.domain.period;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${server.version}/periods")
@RequiredArgsConstructor
public class PeriodController {
    private final PeriodService periodService;

    @GetMapping
    public ResponseEntity<List<PeriodDTO>> getAllPeriods() {
        return ResponseEntity.ok(periodService.getAllPeriods());
    }

    @GetMapping("/get-by-month")
    public ResponseEntity<Period> getPeriodsByMonth(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(periodService.getPeriodByMonthAndYear(month,year));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodDTO> getPeriodById(@PathVariable Long id) {
        return ResponseEntity.ok(periodService.getPeriodById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeriodDTO> createPeriod(@Valid @RequestBody PeriodDTO periodDTO) {
        PeriodDTO createdPeriod = periodService.createPeriod(periodDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPeriod);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeriodDTO> updatePeriod(@PathVariable Long id, @Valid @RequestBody PeriodDTO updatedPeriod) {
        PeriodDTO updated = periodService.updatePeriod(id, updatedPeriod);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        periodService.deletePeriod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unpaid/{userId}")
    public ResponseEntity<List<PeriodDTO>> getUnpaidContributions(@PathVariable Long userId) {
        List<PeriodDTO> unpaidPeriods = periodService.getUnpaidPeriodsByUser(userId);
        return ResponseEntity.ok(unpaidPeriods);
    }
    @GetMapping("/owed/{userId}")
    public ResponseEntity<List<PeriodDTO>> getOwedContributions(@PathVariable Long userId) {
        List<PeriodDTO> unpaidPeriods = periodService.getOwedPeriodsByUser(userId);
        return ResponseEntity.ok(unpaidPeriods);
    }
}
