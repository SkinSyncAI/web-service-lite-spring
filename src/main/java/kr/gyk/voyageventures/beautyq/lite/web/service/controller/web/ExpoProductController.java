package kr.gyk.voyageventures.beautyq.lite.web.service.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.gyk.voyageventures.beautyq.lite.web.service.component.CookieComponent;
import kr.gyk.voyageventures.beautyq.lite.web.service.dto.web.*;
import kr.gyk.voyageventures.beautyq.lite.web.service.service.web.CosmeticService;
import kr.gyk.voyageventures.beautyq.lite.web.service.service.web.ExpoProductService;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.classgen.asm.MopWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expo/product")
public class ExpoProductController {
    private final CookieComponent cookieComponent;
    private final ExpoProductService expoProductService;

    @GetMapping("/{id}")
    public String getExpoProduct (
            Model model,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @PathVariable(name="id") Long id
    ) throws Exception {
        model.addAttribute("product", expoProductService.getCosmeticDetailDTO(id));
        model.addAttribute("productScore", expoProductService.getCosmeticScore(id, cookieComponent.getDiagnosisSkinType(httpServletRequest), cookieComponent.getMainTag(httpServletRequest), cookieComponent.getScoringRandom(httpServletRequest)));
        model.addAttribute("productSim", expoProductService.getCosmeticListSimilarDTO(id, cookieComponent.getDiagnosisSkinType(httpServletRequest), cookieComponent.getMainTag(httpServletRequest), cookieComponent.getScoringRandom(httpServletRequest)));
        model.addAttribute("event", cookieComponent.getEvent(httpServletRequest));
        return "product";
    }

    @GetMapping("/{id}/compare")
    public String getExpoProductCompare (
            Model model,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @PathVariable(name="id") Long id
    ) throws Exception {
        model.addAttribute("productCurrent", expoProductService.getCosmeticScoreListDTOCurrent(id, cookieComponent.getDiagnosisSkinType(httpServletRequest), cookieComponent.getMainTag(httpServletRequest), cookieComponent.getScoringRandom(httpServletRequest)));

        return "product_compare";
    }

    @GetMapping("/{id}/compare/ajax/{tag}")
    public String getExpoProductCompareAjax (
            Model model,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @PathVariable(name="id") Long id,
            @PathVariable(name="tag") Short tag
    ) throws Exception {
        CosmeticScoreListDTO cosmeticListDTO = expoProductService.getCosmeticScoreListDTOSimilar(id, tag, cookieComponent.getDiagnosisSkinType(httpServletRequest), cookieComponent.getMainTag(httpServletRequest), cookieComponent.getScoringRandom(httpServletRequest));
        CosmeticScoreListElementDTO firstDTO = cosmeticListDTO.getCount() > 1 ? cosmeticListDTO.getCosmeticList().getFirst() : null;
        List<CosmeticScoreListElementDTO> newListElementDTO = cosmeticListDTO.getCount() > 1 ? cosmeticListDTO.getCosmeticList().stream().skip(1).toList() : new ArrayList<>();

        model.addAttribute("productTop", firstDTO);
        model.addAttribute("productSim", CosmeticScoreListDTO.builder().count((long) newListElementDTO.size()).cosmeticList(newListElementDTO).build());

        return "product_compare_ajax";
    }

}
